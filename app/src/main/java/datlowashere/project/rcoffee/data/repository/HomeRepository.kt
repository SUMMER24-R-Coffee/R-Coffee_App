package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Banner
import datlowashere.project.rcoffee.data.model.Category
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository {
    private val apiService = ApiClient.instance

    suspend fun getBanners(): List<Banner> = withContext(Dispatchers.IO) {
        apiService.getBanners()
    }

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        apiService.getCategories()
    }

    suspend fun getProducts(email_user: String): List<Product> = withContext(Dispatchers.IO) {
        apiService.getProducts(email_user)
    }
}
