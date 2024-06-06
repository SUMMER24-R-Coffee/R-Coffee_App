package datlowashere.project.rcoffee.data.network

import datlowashere.project.rcoffee.constant.AppConstant
import datlowashere.project.rcoffee.data.model.Banner
import datlowashere.project.rcoffee.data.model.Category
import retrofit2.http.GET

interface ApiService {
    @GET(AppConstant.GET_BANNER)
    suspend fun getBanners(): List<Banner>

    @GET(AppConstant.GET_CATEGORY)
    suspend fun getCategories(): List<Category>
}
