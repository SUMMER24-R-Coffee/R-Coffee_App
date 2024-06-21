package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.data.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {
    private val apiService = ApiClient.instance

    suspend fun getProducts(email_user: String): List<Product> = withContext(Dispatchers.IO) {
        apiService.getProducts(email_user)
    }

    suspend fun getRatings(productId: Int): List<Rating> = withContext(Dispatchers.IO) {
        apiService.getRatings(productId)
    }
}