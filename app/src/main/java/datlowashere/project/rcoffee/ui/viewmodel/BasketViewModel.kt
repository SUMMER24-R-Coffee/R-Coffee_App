package datlowashere.project.rcoffee.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

    private val _deleteResponse = MutableLiveData<Boolean>()
    val deleteResponse: LiveData<Boolean> get() = _deleteResponse

    fun getBaskets(emailUser: String) {
        viewModelScope.launch {
            try {
                val basketsList = basketRepository.getBaskets(emailUser)
                _baskets.postValue(basketsList)
                // Log the baskets list result
                Log.d("BasketViewModel", "Baskets fetched: $basketsList")
            } catch (e: Exception) {
                // Handle error
                Log.e("BasketViewModel", "Error fetching baskets", e)
            }
        }
    }

    fun addBasket(basketItem: Basket) {
        val call = basketRepository.insertBasket(basketItem)
        call.enqueue(object : Callback<Basket> {
            override fun onResponse(call: Call<Basket>, response: Response<Basket>) {
                if (response.isSuccessful) {
                    _basket.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Basket>, t: Throwable) {
                // Handle error
                Log.e("BasketViewModel", "Error adding basket", t)
            }
        })
    }

    fun updateBasket(basketId: Int, basketItem: Basket) {
        val call = basketRepository.updateBasket(basketId, basketItem)
        call.enqueue(object : Callback<Basket> {
            override fun onResponse(call: Call<Basket>, response: Response<Basket>) {
                if (response.isSuccessful) {
                    _basket.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Basket>, t: Throwable) {
                // Handle error
                Log.e("BasketViewModel", "Error updating basket", t)
            }
        })
    }

    fun removeBasket(basketId: Int) {
        val call = basketRepository.deleteBasket(basketId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _deleteResponse.postValue(true)
                } else {
                    _deleteResponse.postValue(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _deleteResponse.postValue(false)
                Log.e("BasketViewModel", "Error removing basket", t)
            }
        })
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
