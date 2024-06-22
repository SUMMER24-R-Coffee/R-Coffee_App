package datlowashere.project.rcoffee.ui.view.activity.order

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.MainActivity
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.data.model.PaymentDetail
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.data.repository.OrderRepository
import datlowashere.project.rcoffee.data.repository.PaymentRepository
import datlowashere.project.rcoffee.databinding.ActivityOrderInnformationBinding
import datlowashere.project.rcoffee.ui.adapter.ItemOrderAdapter
import datlowashere.project.rcoffee.ui.component.CancelOrderBottomSheetDialogFragment
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
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderInnformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderInnformationBinding
    private lateinit var basketViewModel: BasketViewModel
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var itemOrderItemAdapter: ItemOrderAdapter
    private lateinit var orderId: String
    private var totalPayment: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderInnformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        ZaloPaySDK.tearDown();
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        setUpViewModel()

        val order: Order? = intent.getParcelableExtra("ORDER")
        order?.let {
            orderId = it.order_id
            totalPayment = it.total_amount
            displayOrderDetails(it)
        }

        setUpRecyclerView()
        setUpObservers()

        binding.btnBackOrderInf.setOnClickListener {
            finish()
        }

        binding.btnCancelOrder.setOnClickListener {
            handleCancelOrder()
        }

        binding.btnRepay.setOnClickListener {
            zaloPay()
        }
        binding.btnReceiveOrder.setOnClickListener {
            onReceivedOrder()
        }
    }
    //TODO: re-unpaid for order have not paid yet

    private fun setUpRecyclerView() {
        itemOrderItemAdapter = ItemOrderAdapter(this, emptyList())
        binding.rcvOrderItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvOrderItems.adapter = itemOrderItemAdapter
    }

    private fun setUpViewModel() {
        val orderRepository = OrderRepository()
        val orderFactory = OrderViewModelFactory(orderRepository)
        orderViewModel = ViewModelProvider(this, orderFactory).get(OrderViewModel::class.java)

        val repository = BasketRepository()
        val factory = BasketViewModelFactory(repository)
        basketViewModel = ViewModelProvider(this, factory).get(BasketViewModel::class.java)

        val paymentRepository = PaymentRepository()
        val payFactory = PaymentViewModelFactory(paymentRepository)
        paymentViewModel = ViewModelProvider(this, payFactory).get(PaymentViewModel::class.java)
    }

    private fun setUpObservers() {
        basketViewModel.baskets.observe(this, Observer { baskets ->
            baskets?.let {
                itemOrderItemAdapter.updateData(it)
            }
        })

        orderViewModel.getOrders(getEmail())
        basketViewModel.getBasketByOrd(orderId)
    }

    @SuppressLint("SetTextI18n")
    private fun displayOrderDetails(order: Order) {
        binding.tvOrderIDInf.text = order.order_id
        binding.tvTimeOrderInf.text = order.create_at?.let { FormatterHelper.formatDateTime(it) }
        binding.tvUserInforOrdInf.text = "${getName()} | ${getPhone()}"
        binding.tvTotalPaymentOrdInf.text = FormatterHelper.formatCurrency(order.total_amount)
        binding.tvMerchandiseOrdInf.text = FormatterHelper.formatCurrency(order.total_amount + order.discount_amount)
        binding.tvDiscountOrdInf.text = FormatterHelper.formatCurrency(order.discount_amount)
        binding.tvMessageOrdInf.text = order.order_message?.takeIf { it.isNotEmpty() } ?: "None"
        binding.tvPaymentMethodOrdInf.text = order.payment_method
        binding.tvStatusOrderInf.text = order.status_order
        binding.tvAddress.text = order.location
        binding.tvReason.text = order.reason

        when (order.status_order) {
            "cancelled" -> binding.tvStatusOrderInf.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red_exp))
            "delivered" -> binding.tvStatusOrderInf.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green_ext))
            "delivering" -> binding.tvStatusOrderInf.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue_bld))
            else -> binding.tvStatusOrderInf.setTextColor(ContextCompat.getColor(binding.root.context, R.color.yellow_erth))
        }

        binding.apply {
            when (order.status_order) {
                "pending" -> {
                    btnCancelOrder.visibility = View.VISIBLE
                    if (order.payment_status == "unpaid") {
                        btnRepay.visibility = View.VISIBLE
                    }
                }
                "preparing" ->{
                    btnCancelOrder.visibility = View.VISIBLE
                }
                "delivering" -> {
                    btnReceiveOrder.visibility = View.VISIBLE
                }
                "delivered" -> {
                    btnRating.visibility = View.VISIBLE
                }
                else -> {
                    lnBottomOnf.visibility = View.GONE
                }
            }

            if (order.status_order == "cancelled") {
                lnReason.visibility = View.VISIBLE
            }
        }
    }

    private fun handleCancelOrder() {
        val bottomSheet = CancelOrderBottomSheetDialogFragment()
        bottomSheet.setOnReasonSelectedListener(object : CancelOrderBottomSheetDialogFragment.OnReasonSelectedListener {
            override fun onReasonSelected(reason: String) {
                orderViewModel.updateStatusOrder(orderId, "cancelled",reason)
                orderViewModel.statusUpdated.observe(this@OrderInnformationActivity, Observer { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this@OrderInnformationActivity, "Order cancelled successfully", Toast.LENGTH_SHORT).show()
                        navigateToOrderHistory("SWITCH_TO_CANCELLED")
                    } else {
                        Toast.makeText(this@OrderInnformationActivity, "Failed to cancel order", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
        bottomSheet.show(supportFragmentManager, "CancelOrderBottomSheet")
    }

    private fun onReceivedOrder() {
        orderViewModel.updateStatusOrder(orderId, "delivered","")
        orderViewModel.statusUpdated.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Order Received", Toast.LENGTH_SHORT).show()
                navigateToOrderHistory("SWITCH_TO_COMPLETED")
            } else {
                Toast.makeText(this, "Failed to receive order", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToOrderHistory(switchType: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(switchType, true)
        }
        startActivity(intent)
        finish()
    }

    private fun zaloPay(){
        val amountToPay =totalPayment.toInt()
        Log.d("OrderInformation",amountToPay.toString())
        val orderApi = CreateOrder()
        try {
            val data = orderApi.createOrder(amountToPay.toString())
            val code = data.getString("returncode")
            if (code == "1") {
                val token = data.getString("zptranstoken")

                ZaloPaySDK.getInstance().payOrder(this@OrderInnformationActivity, token, "demozpdkt://appt", object :
                    PayOrderListener {
                    override fun onPaymentSucceeded(transactionId: String, transToken: String, appTransID: String) {
                        setStatusPayment("paid")
                        startOrderResultActivity("Success")
                        Toast.makeText(this@OrderInnformationActivity, "Payment Successful", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPaymentCanceled(zpTransToken: String, appTransID: String) {
                        setStatusPayment("unpaid")
                        startOrderResultActivity("Pending")
                        Toast.makeText(this@OrderInnformationActivity, "Payment Cancelled", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPaymentError(zaloPayError: ZaloPayError, zpTransToken: String, appTransID: String) {
                        setStatusPayment("unpaid")
                        startOrderResultActivity("Pending")
                        Toast.makeText(this@OrderInnformationActivity, "Payment Failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun startOrderResultActivity(paymentStatus: String) {
        val intent = Intent(this@OrderInnformationActivity, OrderResultActivity::class.java)
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        intent.putExtra("order_id", orderId)
        intent.putExtra("payment_status", paymentStatus)
        intent.putExtra("name", getName())
        intent.putExtra("phone", getPhone())
        intent.putExtra("address", binding.tvAddress.text.toString())
        intent.putExtra("total_payment", totalPayment)
        intent.putExtra("payment_method", binding.tvPaymentMethodOrdInf.text.toString())
        intent.putExtra("time_create", currentTime)
        intent.putExtra("message",binding.tvMessageOrdInf.text.toString())
        startActivity(intent)
        finish()
    }
    private fun setStatusPayment(status:String){
        var paymentDetail = PaymentDetail(
            status = status,
            order_id = orderId
        )
        paymentViewModel.updatePaymentStatus(orderId,status)
        paymentViewModel.paymentStatus.observe(this@OrderInnformationActivity, Observer {isSuccess ->
            if (isSuccess) {
                Toast.makeText(this@OrderInnformationActivity, "Re-pay order successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@OrderInnformationActivity, "Failed to Re-pay  order", Toast.LENGTH_SHORT).show()
            }
        })
        Log.d("OrderActivityInf", "status: $paymentDetail")

    }

    private fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: ""
    }

    private fun getPhone(): String {
        return SharedPreferencesHelper.getUserPhone(this) ?: ""
    }

    private fun getName(): String {
        return SharedPreferencesHelper.getUserName(this) ?: ""
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

}
