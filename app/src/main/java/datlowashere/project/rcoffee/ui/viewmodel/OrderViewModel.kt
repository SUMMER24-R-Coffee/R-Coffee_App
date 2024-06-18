package datlowashere.project.rcoffee.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.data.repository.OrderRepository
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order> get() = _order

    fun orderItem(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.orderItem(order) { result ->
                _order.postValue(result)
            }
        }
    }
}
class OrderViewModelFactory(private val orderRepository: OrderRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BasketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}