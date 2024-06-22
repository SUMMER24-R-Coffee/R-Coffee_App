package datlowashere.project.rcoffee.ui.view.activity.order

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.MainActivity
import datlowashere.project.rcoffee.constant.AppConstant
import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.data.model.PaymentDetail
import datlowashere.project.rcoffee.data.model.Voucher
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.data.repository.OrderRepository
import datlowashere.project.rcoffee.data.repository.PaymentRepository
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
import datlowashere.project.rcoffee.viewmodel.PaymentViewModel
import datlowashere.project.rcoffee.viewmodel.PaymentViewModelFactory
import datlowashere.project.rcoffee.zalopay.Api.CreateOrder
import datlowashere.project.rcoffee.zalopay.Constant.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var selectedBaskets: ArrayList<Basket>
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var basketViewModel: BasketViewModel
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var listBasketId: List<Int>
    private var totalAmount: Double = 0.0
    private var selectedAddressId: Int? = null
    private var selectedVoucherId: Int? = null
    private var selectedVoucherPercent: Double? = 0.0
    private var discountAmount: Double = 0.0
    private var totalPayment: Double = 0.0
    private var methodPayment: String =""
    private lateinit var orderId: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        setUpView()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX);

        orderId = generateOrderId()
    }

    @SuppressLint("SetTextI18n")
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
            startActivityForResult(intent, AppConstant.REQUEST_CODE_ADDRESS)
        }

        binding.tvSelectVoucher.setOnClickListener {
            val intent = Intent(this, SelectVoucherActivity::class.java)
            startActivityForResult(intent, AppConstant.REQUEST_CODE_VOUCHER)
        }


        binding.btnPlaceOrder.setOnClickListener {
            if (selectedAddressId == null) {
                Toast.makeText(this, "Please, select address", Toast.LENGTH_SHORT).show()
            } else if (!binding.rdoZaloPay.isChecked && !binding.rdoVNPay.isChecked) {
                Toast.makeText(this, "Please, select a payment method", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.rdoZaloPay.isChecked) {
                    methodPayment = "Zalo Pay"
                    placeOrder()
                    zaloPay()
                } else if (binding.rdoVNPay.isChecked) {
                    methodPayment = "VNPay"
                    Toast.makeText(this, "Feature is in progress", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun zaloPay(){
        val amountToPay =totalPayment.toInt()
        Log.d("OrderActivity", "Payment IDs: $amountToPay.tp")
        val orderApi = CreateOrder()
        try {
            val data = orderApi.createOrder(amountToPay.toString())
            val code = data.getString("returncode")
            if (code == "1") {
                val token = data.getString("zptranstoken")

                ZaloPaySDK.getInstance().payOrder(this@OrderActivity, token, "demozpdk://app", object : PayOrderListener {
                    override fun onPaymentSucceeded(transactionId: String, transToken: String, appTransID: String) {
                        setStatusPayment("paid")
                        startOrderResultActivity("Success")
                        Toast.makeText(this@OrderActivity, "Payment Successful", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPaymentCanceled(zpTransToken: String, appTransID: String) {
                        setStatusPayment("unpaid")
                        startOrderResultActivity("Pending")
                        Toast.makeText(this@OrderActivity, "Payment Cancelled", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPaymentError(zaloPayError: ZaloPayError, zpTransToken: String, appTransID: String) {
                        setStatusPayment("unpaid")
                        startOrderResultActivity("Pending")
                        Toast.makeText(this@OrderActivity, "Payment Failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun setUpViewModel(){
        val orderRepository = OrderRepository()
        val orderFactory = OrderViewModelFactory(orderRepository)
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        val repository = BasketRepository()
        val factory = BasketViewModelFactory(repository)
        basketViewModel = ViewModelProvider(this, factory).get(BasketViewModel::class.java)

        val paymentRepository= PaymentRepository()
        val payFactory =PaymentViewModelFactory(paymentRepository)
        paymentViewModel = ViewModelProvider(this, payFactory).get(PaymentViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AppConstant.REQUEST_CODE_ADDRESS -> {
                    val address = data?.getParcelableExtra<Address>("selectedAddress")
                    address?.let {
                        binding.tvAddress.text = it.location
                        selectedAddressId = it.address_id
                    }
                }

                AppConstant.REQUEST_CODE_VOUCHER -> {
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
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }


    private fun startOrderResultActivity(paymentStatus: String) {
        val intent = Intent(this@OrderActivity, OrderResultActivity::class.java)
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        intent.putExtra("order_id", orderId)
        intent.putExtra("payment_status", paymentStatus)
        intent.putExtra("name", getName())
        intent.putExtra("phone", getPhone())
        intent.putExtra("address", binding.tvAddress.text.toString())
        intent.putExtra("total_payment", totalPayment)
        intent.putExtra("payment_method", methodPayment)
        intent.putExtra("time_create", currentTime)
        intent.putExtra("message",binding.edLeaveMessage.text.toString())
        startActivity(intent)
        finish()
    }
    @SuppressLint("SetTextI18n")
    private fun updatePaymentDetails() {
        discountAmount = totalAmount * (selectedVoucherPercent ?: 0.0) / 100
        binding.tvDiscount.text = "-${FormatterHelper.formatCurrency(discountAmount)}"

        totalPayment = totalAmount - discountAmount
        binding.tvTotalPayment.text = FormatterHelper.formatCurrency(totalPayment)
        binding.tvTotalAmountOrder.text = "Total Amount\n${FormatterHelper.formatCurrency(totalPayment)}"
    }

    private fun setStatusPayment(status:String){
        var paymentDetail =PaymentDetail(
            status = status,
            order_id = orderId
        )
        paymentViewModel.insertPaymentDetail(paymentDetail)
        Log.d("OrderActivity", "status: $paymentDetail")

    }
    private fun placeOrder() {
        if (listBasketId.isEmpty()) {
            Log.e("OrderActivity", "Basket ID list is empty, cannot place order")
            return
        }

        val order = Order(
            order_id = orderId,
            update_at = null,
            payment_method = methodPayment,
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
