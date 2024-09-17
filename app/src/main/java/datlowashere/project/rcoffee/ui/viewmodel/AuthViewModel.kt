package datlowashere.project.rcoffee.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.response.ApiResponse
import datlowashere.project.rcoffee.data.model.response.AuthResponse
import datlowashere.project.rcoffee.data.model.response.LoginResponse
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _requestCodeResponse = MutableLiveData<Response<ApiResponse>>()
    val requestCodeResponse: LiveData<Response<ApiResponse>> = _requestCodeResponse

    private val _registerResponse = MutableLiveData<Response<ApiResponse>>()
    val registerResponse: LiveData<Response<ApiResponse>> = _registerResponse
    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> get() = _loginResult


    val requestCodeError: MutableLiveData<String> = MutableLiveData()


    fun requestCode(email: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.requestCode(email)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.status == "success") {
                        Log.d("AuthViewModel", "Verification code sent successfully")
                        _requestCodeResponse.postValue(response)
                    } else {
                        Log.e("AuthViewModel", "Failed to send verification code: ${body?.message}")
                        requestCodeError.postValue(body?.message ?: "Unknown error")
                    }
                } else {
                    Log.e(
                        "AuthViewModel",
                        "Failed to send verification code: ${response.message()}"
                    )
                    requestCodeError.postValue("Failed to send verification code: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "An error occurred: ${e.message}")
                requestCodeError.postValue("An error occurred: ${e.message}")
            }
        }
    }

    fun registerUser(email: String, password: String, code: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.registerUser(email, password, code)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.status == "success") {
                        Log.d("AuthViewModel", "User registered successfully")
                        _registerResponse.postValue(response)
                    } else {
                        Log.e("AuthViewModel", "Failed to register user: ${body?.message}")
                        _registerResponse.postValue(response)
                    }
                } else {
                    Log.e("AuthViewModel", "Failed to register user: ${response.message()}")
                    _registerResponse.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "An error occurred: ${e.message}")
                _registerResponse.postValue(
                    Response.error(
                        500,
                        ResponseBody.create(null, e.message ?: "Unknown error")
                    )
                )
            }
        }
    }


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
        authRepository.getDataUser(userEmail)
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
