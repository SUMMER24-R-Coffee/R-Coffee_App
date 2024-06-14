package datlowashere.project.rcoffee.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import datlowashere.project.rcoffee.data.model.Banner
import datlowashere.project.rcoffee.data.model.Category
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.repository.HomeRepository
import datlowashere.project.rcoffee.utils.Resource
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository,
                    private val context: Context) : ViewModel() {

    private val _banners = MutableLiveData<Resource<List<Banner>>>()
    val banners: LiveData<Resource<List<Banner>>> get() = _banners

    private val _categories = MutableLiveData<Resource<List<Category>>>()
    val categories: LiveData<Resource<List<Category>>> get() = _categories

    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> get() = _products

    val filteredProducts = MutableLiveData<Resource<List<Product>>>()


    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            getBanners()
            getCategories()
        }
    }
    private suspend fun getBanners() {
        _banners.postValue(Resource.Loading())
        try {
            val response = repository.getBanners()
            _banners.postValue(Resource.Success(response))
        } catch (e: Exception) {
            _banners.postValue(Resource.Error(e.message ?: "An error occurred"))
        }
    }

    private suspend fun getCategories() {
        _categories.postValue(Resource.Loading())
        try {
            val response = repository.getCategories()
            _categories.postValue(Resource.Success(response))
        } catch (e: Exception) {
            _categories.postValue(Resource.Error(e.message ?: "An error occurred"))
        }
    }

    fun getProducts(emailUser: String) {
        viewModelScope.launch {
            _products.postValue(Resource.Loading())
            try {
                val response = repository.getProducts(emailUser)
                _products.postValue(Resource.Success(response))
            } catch (e: Exception) {
                _products.postValue(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }

    fun filterProductsByCategory(categoryId: Int) {
        products.value?.data?.let { productList ->
            val filteredList = productList.filter { it.category_id == categoryId }
            filteredProducts.value = Resource.Success(filteredList)
        }
    }
}

class HomeViewModelFactory(private val repository: HomeRepository,
                            private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
