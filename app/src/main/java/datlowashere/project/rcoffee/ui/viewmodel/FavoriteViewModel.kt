package datlowashere.project.rcoffee.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.Favorite
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.data.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    private val _favoriteResponse = MutableLiveData<Favorite>()
    val favoriteResponse: LiveData<Favorite> get() = _favoriteResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun insertOrDeleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            try {
                val response = favoriteRepository.insertOrDelFav(favorite)
                _favoriteResponse.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}

class FavoriteViewModelFactory(private val favoriteRepository: FavoriteRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(favoriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
