package datlowashere.project.rcoffee.ui.view.activity.order

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import datlowashere.project.rcoffee.MainActivity
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.ActivityOrderResultBinding
import datlowashere.project.rcoffee.utils.FormatterHelper

class OrderResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityOrderResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("order_id")
        val paymentStatus = intent.getStringExtra("payment_status")
        val totalPayment = intent.getDoubleExtra("total_payment", 0.0)
        val paymentMethod = intent.getStringExtra("payment_method")
        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val address = intent.getStringExtra("address")
        val time = intent.getStringExtra("time_create")
        val message =intent.getStringExtra("message")


        if(paymentStatus.equals("Pending")){
            val pendingIcon: Drawable? = ContextCompat.getDrawable(this, R.drawable.error_filledsvg)
            binding.fabResult.icon = pendingIcon
            binding.fabResult.iconTint = ContextCompat.getColorStateList(this, R.color.yellow_erth)
        }

        binding.tvORDID.text = orderId
        binding.tvStatusPayment.text = paymentStatus
        binding.tvTotalPaymentR.text = FormatterHelper.formatCurrency(totalPayment)
        binding.tvTimePayment.text = time
        binding.tvPhoneOrder.text = phone
        binding.tvNameOrd.text =name
        binding.tvAddressDeliver.text=address
        binding.tvMessageOrder.text = message ?: "None"
        binding.tvMethodPay.text = paymentMethod

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this@OrderResultActivity, MainActivity::class.java))
            finish()
        }
        binding.btnHomeIcon.setOnClickListener {
            startActivity(Intent(this@OrderResultActivity, MainActivity::class.java))
            finish()
        }
    }
}