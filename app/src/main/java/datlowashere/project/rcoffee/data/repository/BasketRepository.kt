package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.BasketRequest
import datlowashere.project.rcoffee.data.model.BasketUpdateRequest
import datlowashere.project.rcoffee.network.ApiClient
import retrofit2.Call

class BasketRepository() {
    private val apiService = ApiClient.instance
    suspend fun getBaskets(emailUser: String): List<Basket> {
        return apiService.getBaskets(emailUser)
    }
    suspend fun addToBasket(request: BasketRequest): BasketRequest {
        return apiService.addToBasket(request)
    }

    suspend fun updateToBasket(basket: BasketUpdateRequest): BasketUpdateRequest {
        return apiService.updateToBasket(basket)
    }

    fun deleteBasket(basketId: Int): Call<Void> {
        return apiService.deleteBasket(basketId)
    }
}
