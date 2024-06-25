package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.data.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RatingRepository {
    private val apiService = ApiClient.instance

    suspend fun getRatings(productId: Int): List<Rating> = withContext(Dispatchers.IO) {
        apiService.getRatings(productId)
    }

    suspend fun getRatingsByBasketIds(basketIds: List<Int>): List<Rating> = withContext(Dispatchers.IO) {
        val basketIdsString = basketIds.joinToString(",")
        apiService.getRatingsByBasketIds(basketIdsString)
    }
    suspend fun insertRating(rating: Rating): Rating {
        return apiService.insertRating(rating)
    }
}