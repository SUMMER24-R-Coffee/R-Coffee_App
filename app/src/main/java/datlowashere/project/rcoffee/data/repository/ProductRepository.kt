package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Banner
import datlowashere.project.rcoffee.data.model.Category
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.network.ApiClient
import datlowashere.project.rcoffee.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {
    private val apiService = ApiClient.instance

    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        apiService.getProducts()
    }

    suspend fun getRatings(productId: Int): List<Rating> = withContext(Dispatchers.IO) {
        apiService.getRatings(productId)
    }
}