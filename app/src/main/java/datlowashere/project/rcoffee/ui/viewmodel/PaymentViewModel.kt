package datlowashere.project.rcoffee.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.PaymentDetail
import datlowashere.project.rcoffee.data.model.response.PaymentIntentResponse
import datlowashere.project.rcoffee.data.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel(private val paymentRepository: PaymentRepository) : ViewModel() {

    private val _paymentStatus = MutableLiveData<Boolean>()
    val paymentStatus: LiveData<Boolean> get() = _paymentStatus

    fun insertPaymentDetail(payment: PaymentDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            paymentRepository.insertPaymentDetail(payment) { result ->
                _paymentStatus.postValue(result)
            }
        }
    }
    fun updatePaymentStatus(orderId: String, status: String, emailUser:String, token: String) {
        var paymentDetail = PaymentDetail(
            payment_id = 0,
            status = status,
            order_id = orderId,
            email_user = emailUser,
            token= token
        )
        viewModelScope.launch(Dispatchers.IO) {
            paymentRepository.updatePaymentStatus(orderId, paymentDetail) { isSuccess ->
                Log.d("PaymentViewModel", "success")
                _paymentStatus.postValue(isSuccess)
            }
        }
    }
    fun createPaymentIntent(params: Map<String, String>): LiveData<PaymentIntentResponse> {
        return paymentRepository.createPaymentIntent(params)
    }

}

class PaymentViewModelFactory(private val paymentRepository: PaymentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaymentViewModel(paymentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
