package datlowashere.project.rcoffee.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.data.repository.ProductRepository
import datlowashere.project.rcoffee.utils.Resource
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository, ) : ViewModel()  {

    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> get() = _products

    private val _ratings = MutableLiveData<Resource<List<Rating>>>()
    val ratings: LiveData<Resource<List<Rating>>> get() = _ratings



    fun getProducts(email_user: String) {
        viewModelScope.launch {
            _products.postValue(Resource.Loading())
            try {
                val response = repository.getProducts(email_user)
                _products.postValue(Resource.Success(response))
            } catch (e: Exception) {
                _products.postValue(Resource.Error(e.message ?: "An error occurred"))
            }
        }

    }

    fun getRatings(productId: Int) {
        viewModelScope.launch {
            _ratings.postValue(Resource.Loading())
            try {
                val response = repository.getRatings(productId)
                _ratings.postValue(Resource.Success(response))
            } catch (e: Exception) {
                _ratings.postValue(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }

}

class ProductViewModelFactory( private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

