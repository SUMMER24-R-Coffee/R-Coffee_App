package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Category
import datlowashere.project.rcoffee.network.ApiClient

class HomeRepository {
    private val apiService = ApiClient.instance

    suspend fun getBanners() = apiService.getBanners()

    suspend fun getCategories() =apiService.getCategories()
}