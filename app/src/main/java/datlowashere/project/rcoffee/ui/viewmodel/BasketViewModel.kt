package datlowashere.project.rcoffee.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import datlowashere.project.rcoffee.data.model.ApiResponse
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.repository.BasketRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BasketViewModel(private val basketRepository: BasketRepository) : ViewModel() {

    private val _baskets = MutableLiveData<List<Basket>>()
    val baskets: LiveData<List<Basket>> get() = _baskets

    private val _basket = MutableLiveData<Basket>()
    val basket: LiveData<Basket> get() = _basket

    private val _addOrUpdateResponse = MutableLiveData<Boolean>()
    val addOrUpdateResponse: LiveData<Boolean> get() = _addOrUpdateResponse

    private val _updateToBasket = MutableLiveData<Boolean>()
    val updateToBasket: LiveData<Boolean> get() = _updateToBasket


    private val _updateQuantity = MutableLiveData<Boolean>()
    val updateQuantity: LiveData<Boolean> = _updateQuantity

    private val _toastMessage = MutableLiveData<String>()


    private val _deleteResponse = MutableLiveData<Boolean>()
    val deleteResponse: LiveData<Boolean> get() = _deleteResponse
    val toastMessage: LiveData<String> get() = _toastMessage

    fun getBaskets(emailUser: String) {
        viewModelScope.launch {
            try {
                val basketsList = basketRepository.getBaskets(emailUser)
                _baskets.postValue(basketsList)
            } catch (e: Exception) {
                _toastMessage.postValue("Error fetching baskets: ${e.message}")
            }
        }
    }

    fun addOrUpdateBasket(producId: Int, emailUser: String) {
        viewModelScope.launch {
            try {
                val basketItem = Basket(
                    product_id = producId,
                    email_user = emailUser
                )
                val response = basketRepository.addToBasket(basketItem)
                _addOrUpdateResponse.postValue(true)
                _toastMessage.postValue("Basket added successfully")
            } catch (e: Exception) {
                _addOrUpdateResponse.postValue(false)
                _toastMessage.postValue("Error adding basket: ${e.message}")
            }
        }
    }

    fun updateOrderIDBasket(orderId: String, basketId: List<Int>): LiveData<Boolean> {
        return basketRepository.updateOrderIDBasket(orderId, basketId)
    }
    fun updateQuantity(basketId: Int, quantity: Int) {
        val basket = Basket(
            quantity = quantity,
            basket_id = basketId
        )
        basketRepository.updateQuanty(basketId, basket) { response ->
            _updateQuantity.postValue(true)
        }
    }

    fun updateToBasket(quantity: Int, product_id: Int, email_user: String) {
        viewModelScope.launch {
            try {
                val basketItem = Basket(
                    quantity = quantity,
                    product_id = product_id,
                    email_user = email_user
                )
                Log.d("BasketUpdate", "Basket Item: $basketItem")

                val response = basketRepository.updateToBasket(basketItem)
                _updateToBasket.postValue(true)
                _toastMessage.postValue("Basket updated successfully")
            } catch (e: Exception) {
                Log.e("BasketUpdate", "Error updating basket: ${e.message}", e)
                _updateToBasket.postValue(false)
                _toastMessage.postValue("Error updating basket: ${e.message}")
            }
        }
    }


    fun removeBasket(basketId: Int, callback: (Boolean) -> Unit) {
        val call = basketRepository.deleteBasket(basketId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _toastMessage.postValue("Basket removed successfully")
                    callback(true)
                } else {
                    _toastMessage.postValue("Failed to remove basket: ${response.message()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _toastMessage.postValue("Error removing basket: ${t.message}")
                callback(false)
            }
        })
    }
    fun clearToastMessage() {
        _toastMessage.value = null
    }
}

class BasketViewModelFactory(private val basketRepository: BasketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BasketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BasketViewModel(basketRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
