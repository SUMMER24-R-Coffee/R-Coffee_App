package datlowashere.project.rcoffee.ui.view.activity.voucher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.data.model.Voucher
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.data.repository.VoucherRepository
import datlowashere.project.rcoffee.databinding.ActivitySelectVoucherBinding
import datlowashere.project.rcoffee.ui.adapter.VoucherAdapter
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModelFactory
import datlowashere.project.rcoffee.viewmodel.VoucherViewModel
import datlowashere.project.rcoffee.viewmodel.VoucherViewModelFactory

class SelectVoucherActivity : AppCompatActivity(), VoucherAdapter.OnItemClickListener {
    private lateinit var binding: ActivitySelectVoucherBinding
    private lateinit var voucherViewModel:VoucherViewModel
    private lateinit var voucherAdapter: VoucherAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = VoucherRepository()
        val factory = VoucherViewModelFactory(repository)
        voucherViewModel = ViewModelProvider(this, factory).get(VoucherViewModel::class.java)

        binding.btnBackSelectVoucher.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        observeViewModel()
        voucherViewModel.getVouchers()
    }

    private fun setupRecyclerView() {
        voucherAdapter = VoucherAdapter(emptyList(), this)
        binding.rcvSelectVoucher.apply {
            layoutManager = LinearLayoutManager(this@SelectVoucherActivity)
            adapter = voucherAdapter
        }
    }

    private fun observeViewModel() {
        voucherViewModel.vouchers.observe(this, Observer { vouchers ->
            vouchers?.let {
                voucherAdapter = VoucherAdapter(it, this@SelectVoucherActivity)
                binding.rcvSelectVoucher.adapter = voucherAdapter
            }
        })
    }

    override fun onItemClick(voucher: Voucher) {
        val resultIntent = Intent()
        resultIntent.putExtra("selectedVoucher", voucher)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
