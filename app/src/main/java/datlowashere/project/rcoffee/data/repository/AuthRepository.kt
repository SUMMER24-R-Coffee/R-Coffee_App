package datlowashere.project.rcoffee.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import datlowashere.project.rcoffee.data.model.response.AuthResponse
import datlowashere.project.rcoffee.data.model.response.LoginResponse
import datlowashere.project.rcoffee.data.model.Users
import datlowashere.project.rcoffee.data.model.response.ApiResponse
import datlowashere.project.rcoffee.data.model.response.RegisterResponese
import datlowashere.project.rcoffee.data.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {
    private val apiService = ApiClient.instance

    private val _userResponse = MutableLiveData<AuthResponse>()
    val userResponse: LiveData<AuthResponse>
        get() = _userResponse
    suspend fun login(email_user: String, password: String): Response<LoginResponse> = withContext(Dispatchers.IO) {
        val user = Users(email_user = email_user, password = password)
        apiService.loginUser(user)
    }

    fun getDataUser(userEmail: String) {
        apiService.getUser(userEmail).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    _userResponse.postValue(response.body())
                } else {
                    Log.e("AuthRepository", "Error: ${response.code()} - ${response.message()}")
                    _userResponse.postValue(null)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("AuthRepository", "Failure: ${t.message}", t)
                _userResponse.postValue(null)
            }
        })
    }
    suspend fun requestCode(email: String): Response<ApiResponse> {
        return try {
            val response = apiService.requestCode(RegisterResponese(email))
            Log.d("AuthRepository", "Request code response: ${response.message()}, body: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error requesting code", e)
            throw e
        }
    }

    suspend fun registerUser(email: String, password: String, code: String): Response<ApiResponse> {
        return try {
            val response = apiService.registerUser(RegisterResponese(email, password, code))
            Log.d("AuthRepository", "Register user response: ${response.message()}, body: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error registering user", e)
            throw e
        }
    }


}
