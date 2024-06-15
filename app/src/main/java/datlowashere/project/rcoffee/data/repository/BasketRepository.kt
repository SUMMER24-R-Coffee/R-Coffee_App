package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.network.ApiClient
import retrofit2.Call

class BasketRepository() {
    private val apiService = ApiClient.instance
    suspend fun getBaskets(emailUser: String): List<Basket> {
        return apiService.getBaskets(emailUser)
    }

    fun insertBasket(basketItem: Basket): Call<Basket> {
        return apiService.insertBasket(basketItem)
    }

    fun updateBasket(basketId: Int, basketItem: Basket): Call<Basket> {
        return apiService.updateBasket(basketId, basketItem)
    }

    fun deleteBasket(basketId: Int): Call<Void> {
        return apiService.deleteBasket(basketId)
    }
}
