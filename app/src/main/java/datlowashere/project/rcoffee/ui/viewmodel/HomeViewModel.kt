package datlowashere.project.rcoffee.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import datlowashere.project.rcoffee.data.model.Banner
import datlowashere.project.rcoffee.data.model.Category
import datlowashere.project.rcoffee.data.repository.HomeRepository
import datlowashere.project.rcoffee.utils.Resource
import kotlinx.coroutines.launch
import java.util.*
class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _banners: MutableLiveData<Resource<List<Banner>>> = MutableLiveData()
    val banners: LiveData<Resource<List<Banner>>>
        get() = _banners

    private val _categories: MutableLiveData<Resource<List<Category>>> = MutableLiveData()
    val categories: LiveData<Resource<List<Category>>>
        get() = _categories

    init {
        getBanner()
        getCategories()
    }

    fun getBanner() = viewModelScope.launch {
        _banners.value = Resource.Loading()
        try {
            val response = repository.getBanners()
            _banners.value = Resource.Success(response)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Failed to fetch banners", e)
            _banners.value = Resource.Error("Failed to fetch banners: ${e.message}")
        }
    }

    fun getCategories() = viewModelScope.launch {
        _categories.value = Resource.Loading()
        try {
            val response = repository.getCategories()
            _categories.value = Resource.Success(response)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Failed to fetch categories", e)
            _categories.value = Resource.Error("Failed to fetch categories: ${e.message}")
        }
    }
}

class HomeViewModelFactory(private val repository: HomeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
