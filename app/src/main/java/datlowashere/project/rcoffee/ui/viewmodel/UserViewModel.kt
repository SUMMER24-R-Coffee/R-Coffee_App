package datlowashere.project.rcoffee.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.response.TokenResponse
import datlowashere.project.rcoffee.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _updateTokenResult = MutableLiveData<Result<Void?>>()
    val updateTokenResult: LiveData<Result<Void?>> get() = _updateTokenResult

    fun updateTokenUser(emailUser: String, tokenResponse: TokenResponse) {
        viewModelScope.launch {
            val result = userRepository.updateTokenUser(emailUser, tokenResponse)
            _updateTokenResult.value = result
        }
    }
}
class UserViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}