package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Users
import datlowashere.project.rcoffee.data.model.response.ApiResponse
import datlowashere.project.rcoffee.data.model.response.TokenResponse
import datlowashere.project.rcoffee.data.network.ApiClient
import retrofit2.Response

class UserRepository {
    private val apiService = ApiClient.instance

    suspend fun updateTokenUser(emailUser: String, tokenResponse: TokenResponse): Result<Void?> {
        return try {
            val response: Response<Void> = apiService.updateTokenUser(emailUser, tokenResponse).execute()
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Error updating token: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: Users): Response<ApiResponse> {
        return apiService.updateUser(user)
    }

    suspend fun updatePassword(email_user: String, password: String): Response<ApiResponse> {
        val params = mapOf("email_user" to email_user, "password" to password)
        return apiService.updatePassword(params)
    }

}