package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Favorite
import datlowashere.project.rcoffee.data.network.ApiClient

class FavoriteRepository {
    private val apiService = ApiClient.instance

    suspend fun insertOrDelFav(favorite: Favorite): Favorite{
        return apiService.insertOrDelFav(favorite)
    }
}