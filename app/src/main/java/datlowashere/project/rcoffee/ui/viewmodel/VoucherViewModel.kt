package datlowashere.project.rcoffee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.Voucher
import datlowashere.project.rcoffee.data.repository.VoucherRepository
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoucherViewModel(private val voucherRepository: VoucherRepository) : ViewModel() {

    private val _vouchers = MutableLiveData<List<Voucher>>()
    val vouchers: LiveData<List<Voucher>> get() = _vouchers

    fun getVouchers() {
        viewModelScope.launch(Dispatchers.IO) {
            voucherRepository.getVouchers { result ->
                _vouchers.postValue(result)
            }
        }
    }
}

class VoucherViewModelFactory(private val voucherRepository: VoucherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoucherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoucherViewModel(voucherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}