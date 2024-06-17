package datlowashere.project.rcoffee.ui.view.activity.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    private lateinit var binding :ActivityOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedBaskets = intent.getParcelableArrayListExtra<Basket>("selectedBaskets")
        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)

        for (basket in selectedBaskets.orEmpty()) {
            Log.d("OrderActivity", "Selected Basket: $basket")
        }

        Log.d("OrderActivity", "Total Amount: $totalAmount")

        binding.btnBackOrder.setOnClickListener {
            finish()
        }
    }
}