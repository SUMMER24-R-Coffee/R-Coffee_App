package datlowashere.project.rcoffee.ui.view.activity.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.databinding.ActivityOrderBinding
import datlowashere.project.rcoffee.ui.adapter.ItemOrderAdapter

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var selectedBaskets: ArrayList<Basket>
    private var totalAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedBaskets = intent.getParcelableArrayListExtra<Basket>("selectedBaskets") ?: arrayListOf()
        totalAmount = intent.getDoubleExtra("totalAmount", 0.0)

        for (basket in selectedBaskets) {
        }

        val adapter = ItemOrderAdapter(this, selectedBaskets)
        binding.rcvSelectedBasket.layoutManager = LinearLayoutManager(this)
        binding.rcvSelectedBasket.adapter = adapter

        binding.btnBackOrder.setOnClickListener {
            finish()
        }
    }
}
