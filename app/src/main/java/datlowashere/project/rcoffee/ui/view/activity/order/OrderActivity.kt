package datlowashere.project.rcoffee.ui.view.activity.order

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.data.model.Voucher
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.data.repository.OrderRepository
import datlowashere.project.rcoffee.databinding.ActivityOrderBinding
import datlowashere.project.rcoffee.ui.adapter.ItemOrderAdapter
import datlowashere.project.rcoffee.ui.view.activity.address.AddressActivity
import datlowashere.project.rcoffee.ui.view.activity.address.SelectAddressActivity
import datlowashere.project.rcoffee.ui.view.activity.voucher.SelectVoucherActivity
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModelFactory
import datlowashere.project.rcoffee.utils.FormatterHelper
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper
import datlowashere.project.rcoffee.viewmodel.OrderViewModel
import datlowashere.project.rcoffee.viewmodel.OrderViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var selectedBaskets: ArrayList<Basket>
    private var totalAmount: Double = 0.0
    private var selectedAddressId: Int? = null
    private var selectedVoucherId: Int? = null
    private var selectedVoucherPercent: Double? = 0.0
    private var discountAmount: Double = 0.0
    private var totalPayment: Double = 0.0
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var basketViewModel: BasketViewModel
    private lateinit var listBasketId: List<Int>

    companion object {
        const val REQUEST_CODE_ADDRESS = 1
        const val REQUEST_CODE_VOUCHER = 2
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        setUpView()
    }

    private fun setUpView(){
        binding.tvInforUserOrd.text = "Delivery Address\n${getName()} | ${getPhone()}"

        selectedBaskets = intent.getParcelableArrayListExtra<Basket>("selectedBaskets") ?: arrayListOf()
        totalAmount = intent.getDoubleExtra("totalAmount", 0.0)

        listBasketId = selectedBaskets.map { it.basket_id }
        Log.d("OrderActivity", "List of Basket IDs: $listBasketId")

        updatePaymentDetails()

        binding.tvMerchandise.text = FormatterHelper.formatCurrency(totalAmount)
        binding.tvShipping.text = "0 đ"
        val adapter = ItemOrderAdapter(this, selectedBaskets)
        binding.rcvSelectedBasket.layoutManager = LinearLayoutManager(this)
        binding.rcvSelectedBasket.adapter = adapter

        binding.btnBackOrder.setOnClickListener {
            finish()
        }

        binding.lnSelectAddress.setOnClickListener {
            val intent = Intent(this, SelectAddressActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADDRESS)
        }

        binding.tvSelectVoucher.setOnClickListener {
            val intent = Intent(this, SelectVoucherActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_VOUCHER)
        }

        binding.btnPlaceOrder.setOnClickListener {
            if (selectedAddressId == null ) {
                Toast.makeText(this, "Please, select address", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()

            }
        }
    }



    private fun setUpViewModel(){
        val orderRepository = OrderRepository()
        val orderFactory = OrderViewModelFactory(orderRepository)
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        val repository = BasketRepository()
        val factory = BasketViewModelFactory(repository)
        basketViewModel = ViewModelProvider(this, factory).get(BasketViewModel::class.java)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_ADDRESS -> {
                    val address = data?.getParcelableExtra<Address>("selectedAddress")
                    address?.let {
                        binding.tvAddress.text = it.location
                        selectedAddressId = it.address_id
                    }
                }

                REQUEST_CODE_VOUCHER -> {
                    val voucher = data?.getParcelableExtra<Voucher>("selectedVoucher")
                    voucher?.let {
                        binding.tvSelectVoucher.text = it.voucher_code ?: "No voucher selected"
                        selectedVoucherId = it.voucher_id
                        selectedVoucherPercent = it.percent
                        updatePaymentDetails()
                    }
                }
            }
        }
    }

    private fun updatePaymentDetails() {
        discountAmount = totalAmount * (selectedVoucherPercent ?: 0.0) / 100
        binding.tvDiscount.text = "-${FormatterHelper.formatCurrency(discountAmount)}"

        totalPayment = totalAmount - discountAmount
        binding.tvTotalPayment.text = FormatterHelper.formatCurrency(totalPayment)
        binding.tvTotalAmountOrder.text = "Total Amount\n${FormatterHelper.formatCurrency(totalPayment)}"
    }

    private fun placeOrder() {
        if (listBasketId.isEmpty()) {
            Log.e("OrderActivity", "Basket ID list is empty, cannot place order")
            return
        }

        val order = Order(
            order_id = generateOrderId(),
            update_at = null,
            payment_method = "Zalo Pay",
            total_amount = totalPayment,
            discount_amount = discountAmount,
            address_id = selectedAddressId,
            voucher_id = selectedVoucherId,
            order_message = binding.edLeaveMessage.text.toString(),
            basket_id = listBasketId
        )

        orderViewModel.insertOrder(order)

        Log.d("OrderActivity", "Order: $order")
        Log.d("OrderActivity", "Basket IDs: $listBasketId")
    }

    private fun generateOrderId(): String {
        val currentDateTime = Date()
        val dateFormat = SimpleDateFormat("yyMMddHHmmss", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDateTime)
        return "ORD$formattedDate"
    }

    private fun getPhone(): String {
        return SharedPreferencesHelper.getUserPhone(this) ?: " "
    }

    private fun getName(): String {
        return SharedPreferencesHelper.getUserName(this) ?: " "
    }
}
