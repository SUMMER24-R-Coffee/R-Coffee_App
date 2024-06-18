package datlowashere.project.rcoffee.ui.view.activity.order

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.Voucher
import datlowashere.project.rcoffee.databinding.ActivityOrderBinding
import datlowashere.project.rcoffee.ui.adapter.ItemOrderAdapter
import datlowashere.project.rcoffee.ui.view.activity.address.AddressActivity
import datlowashere.project.rcoffee.ui.view.activity.address.SelectAddressActivity
import datlowashere.project.rcoffee.ui.view.activity.voucher.SelectVoucherActivity

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var selectedBaskets: ArrayList<Basket>
    private var totalAmount: Double = 0.0

    companion object {
        const val REQUEST_CODE_ADDRESS = 1
        const val REQUEST_CODE_VOUCHER = 2
    }
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

        binding.lnSelectAddress.setOnClickListener {
            val intent = Intent(this, SelectAddressActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADDRESS)
        }

        binding.tvSelectVoucher.setOnClickListener {
            val intent = Intent(this, SelectVoucherActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_VOUCHER)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_ADDRESS -> {
                    val address = data?.getParcelableExtra<Address>("selectedAddress")
                    binding.tvAddress.text = address?.location
                }
                REQUEST_CODE_VOUCHER -> {
                    val voucher = data?.getParcelableExtra<Voucher>("selectedVoucher")
                    binding.tvSelectVoucher.text = voucher?.voucher_code ?: "No voucher selected"
                }
            }
        }
    }
}
