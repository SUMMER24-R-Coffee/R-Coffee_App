package datlowashere.project.rcoffee.ui.view.activity.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.data.repository.AddressRepository
import datlowashere.project.rcoffee.databinding.ActivityAddAddressBinding
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper
import datlowashere.project.rcoffee.viewmodel.AddressViewModel
import datlowashere.project.rcoffee.viewmodel.AddressViewModelFactory

class AddAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAddressBinding
    private lateinit var addressViewModel: AddressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AddressRepository()
        val factory = AddressViewModelFactory(repository)
        addressViewModel = ViewModelProvider(this, factory).get(AddressViewModel::class.java)

        binding.btnBackAddAdress.setOnClickListener {
            finish()
        }


        binding.btnSaveLocation.setOnClickListener {
            val addressText = binding.edLocation.text.toString()
            if (addressText.isNotBlank()) {
                val newAddress = Address(addressText,getEmail())
                addressViewModel.addAddress(newAddress)
                finish()
            }else{
                Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show()

            }
        }

        addressViewModel.addedAddress.observe(this) {
        }
    }

    private fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: " "
    }

}
