package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.ApiResponse
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BasketRepository() {
    private val apiService = ApiClient.instance
    suspend fun getBaskets(emailUser: String): List<Basket> {
        return apiService.getBaskets(emailUser)
    }
    suspend fun addToBasket(basket: Basket): Basket {
        return apiService.addToBasket(basket)
    }

    suspend fun updateToBasket(basket: Basket): Basket {
        return apiService.updateToBasket(basket)
    }

    fun updateQuanty(basketId: Int, basket: Basket, callback: (ApiResponse?) -> Unit) {
        apiService.updateQuanty(basketId, basket).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(ApiResponse(status = "error", message = "Failed to update basket"))
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                callback(ApiResponse(status = "error", message = t.message ?: ""))
            }
        })
    }
    fun deleteBasket(basketId: Int): Call<Void> {
        return apiService.deleteBasket(basketId)
    }
}
