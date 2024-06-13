package datlowashere.project.rcoffee.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.AuthResponse
import datlowashere.project.rcoffee.data.model.LoginResponse
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> get() = _loginResult


    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Resource.Loading()
            try {
                val response = authRepository.login(email, password)
                if (response.isSuccessful) {
                    _loginResult.value = Resource.Success(response.body()!!)
                } else {
                    _loginResult.value = Resource.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _loginResult.value = Resource.Error("An error occurred: ${e.message}")
            }
        }
    }

    val userResponse: LiveData<AuthResponse>
        get() = authRepository.userResponse

    fun getUserData(userEmail: String) {
        authRepository.fetchUserData(userEmail)
    }
}

class AuthViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
