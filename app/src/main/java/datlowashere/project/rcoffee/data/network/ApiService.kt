package datlowashere.project.rcoffee.data.network

import datlowashere.project.rcoffee.constant.AppConstant
import datlowashere.project.rcoffee.data.model.Banner
import datlowashere.project.rcoffee.data.model.Category
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.model.Rating
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET(AppConstant.GET_BANNER)
    suspend fun getBanners(): List<Banner>

    @GET(AppConstant.GET_CATEGORY)
    suspend fun getCategories(): List<Category>

    @GET(AppConstant.GET_PRODUCT)
    suspend fun getProducts(): List<Product>

    @GET(AppConstant.GET_RATING)
    suspend fun getRatings(@Path("product_id") productId: Int): List<Rating>
}
