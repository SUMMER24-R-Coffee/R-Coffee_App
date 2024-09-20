package datlowashere.project.rcoffee.ui.view.activity.address

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.data.repository.AddressRepository
import datlowashere.project.rcoffee.databinding.ActivitySelectAddressBinding
import datlowashere.project.rcoffee.ui.adapter.AddressAdapter
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper
import datlowashere.project.rcoffee.viewmodel.AddressViewModel
import datlowashere.project.rcoffee.viewmodel.AddressViewModelFactory

class SelectAddressActivity : AppCompatActivity(), AddressAdapter.OnItemClickListener {
    private lateinit var binding: ActivitySelectAddressBinding
    private lateinit var addressViewModel: AddressViewModel
    private lateinit var addressAdapter: AddressAdapter
    private var allAddresses: List<Address> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = AddressRepository()
        val factory = AddressViewModelFactory(repository)
        addressViewModel = ViewModelProvider(this, factory).get(AddressViewModel::class.java)


        binding.btnBackSelectAddress.setOnClickListener {
            finish()
        }

        binding.btnAddAdress.setOnClickListener {
            val intent = Intent(this, AddAddressActivity::class.java)
            startActivity(intent)
        }

        binding.edSearchAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterAddresses(s.toString())
            }
        })
        setupRecyclerView()
        observeViewModel()
        addressViewModel.getAddresses(getEmail())
    }

    private fun setupRecyclerView() {
        addressAdapter = AddressAdapter(emptyList(), this)
        binding.rcvSelectAddress.apply {
            layoutManager = LinearLayoutManager(this@SelectAddressActivity)
            adapter = addressAdapter
        }

    }

    private fun observeViewModel() {
        addressViewModel.addresses.observe(this, Observer { addresses ->
            addresses?.let {
                allAddresses = it
                filterAddresses(binding.edSearchAddress.text.toString())
            }
        })
    }

    private fun filterAddresses(query: String) {
        val filteredAddresses = if (query.isEmpty()) {
            allAddresses
        } else {
            allAddresses.filter {
                it.location.contains(query, ignoreCase = true)
            }
        }
        addressAdapter.updateAddresses(filteredAddresses)
    }

    override fun onItemClick(address: Address) {
        val resultIntent = Intent()
        resultIntent.putExtra("selectedAddress", address)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: " "
    }

    override fun onResume() {
        super.onResume()
        addressViewModel.getAddresses(getEmail())
    }
}