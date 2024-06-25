package datlowashere.project.rcoffee.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.data.repository.RatingRepository
import datlowashere.project.rcoffee.utils.Resource
import kotlinx.coroutines.launch

class RatingViewModel(private val repository: RatingRepository, ) : ViewModel()  {

    private val _ratings = MutableLiveData<Resource<List<Rating>>>()
    val ratings: LiveData<Resource<List<Rating>>> get() = _ratings

    private val _insertRatingResult = MutableLiveData<Resource<Rating>>()
    val insertRatingResult: LiveData<Resource<Rating>> get() = _insertRatingResult

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


    fun getRatingsByBasketIds(basketIds: List<Int>) {
        viewModelScope.launch {
            _ratings.postValue(Resource.Loading())
            try {
                val response = repository.getRatingsByBasketIds(basketIds)
                _ratings.postValue(Resource.Success(response))
                println("Fetched Ratings: $response") // Log the response
            } catch (e: Exception) {
                _ratings.postValue(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }
    fun insertRating(rating: Rating) {
        viewModelScope.launch {
            _insertRatingResult.postValue(Resource.Loading())
            try {
                val result = repository.insertRating(rating)
                _insertRatingResult.postValue(Resource.Success(result))
            } catch (e: Exception) {
                _insertRatingResult.postValue(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }
}

class RatingViewModelFactory( private val repository: RatingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RatingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RatingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}