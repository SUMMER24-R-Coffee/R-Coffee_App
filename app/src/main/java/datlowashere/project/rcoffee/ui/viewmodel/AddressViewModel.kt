package datlowashere.project.rcoffee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.data.repository.AddressRepository
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressViewModel(private val addressRepository: AddressRepository) : ViewModel() {

    private val _addresses = MutableLiveData<List<Address>>()
    val addresses: LiveData<List<Address>> get() = _addresses

    private val _addedAddress = MutableLiveData<Address>()
    val addedAddress: LiveData<Address> get() = _addedAddress

    fun getAddresses(emailUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addressRepository.getAddresses(emailUser) { result ->
                _addresses.postValue(result)
            }
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            addressRepository.addAddress(address) { result ->
                _addedAddress.postValue(result)
            }
        }
    }
}

class AddressViewModelFactory(private val addressRepository: AddressRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddressViewModel(addressRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}