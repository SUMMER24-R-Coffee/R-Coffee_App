package datlowashere.project.rcoffee.ui.view.activity.order

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import datlowashere.project.rcoffee.MainActivity
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.ActivityOrderResultBinding
import datlowashere.project.rcoffee.utils.FormatterHelper

class OrderResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("order_id") ?: "None"
        val paymentStatus = intent.getStringExtra("payment_status") ?: "None"
        val totalPayment = intent.getDoubleExtra("total_payment", 0.0)
        val paymentMethod = intent.getStringExtra("payment_method") ?: "None"
        val name = intent.getStringExtra("name") ?: "None"
        val phone = intent.getStringExtra("phone") ?: "None"
        val address = intent.getStringExtra("address") ?: "None"
        val time = intent.getStringExtra("time_create") ?: "None"
        val message = intent.getStringExtra("message") ?: "None"

        // Log received data
        Log.d("OrderResultActivity", "Received data from intent:")
        Log.d("OrderResultActivity", "order_id: $orderId")
        Log.d("OrderResultActivity", "payment_status: $paymentStatus")
        Log.d("OrderResultActivity", "total_payment: $totalPayment")
        Log.d("OrderResultActivity", "payment_method: $paymentMethod")
        Log.d("OrderResultActivity", "name: $name")
        Log.d("OrderResultActivity", "phone: $phone")
        Log.d("OrderResultActivity", "address: $address")
        Log.d("OrderResultActivity", "time_create: $time")
        Log.d("OrderResultActivity", "message: $message")

        if (paymentStatus.equals("Pending", ignoreCase = true)) {
            val pendingIcon: Drawable? = ContextCompat.getDrawable(this, R.drawable.error_filledsvg)
            binding.fabResult.setImageDrawable(pendingIcon)
            binding.fabResult.imageTintList = ContextCompat.getColorStateList(this, R.color.yellow_erth)
        }

        binding.tvORDID.text = orderId
        binding.tvStatusPayment.text = paymentStatus
        binding.tvTotalPaymentR.text = FormatterHelper.formatCurrency(totalPayment)
        binding.tvTimePayment.text = time
        binding.tvPhoneOrder.text = phone
        binding.tvNameOrd.text = name
        binding.tvAddressDeliver.text = address
        binding.tvMessageOrder.text = message
        binding.tvMethodPay.text = paymentMethod

        binding.btnHome.setOnClickListener {
            navigateToMainActivity()
        }

        binding.btnHomeIcon.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this@OrderResultActivity, MainActivity::class.java))
        finish()
    }
}
