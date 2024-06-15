package datlowashere.project.rcoffee.data.network

import datlowashere.project.rcoffee.constant.AppConstant
import datlowashere.project.rcoffee.data.model.AuthResponse
import datlowashere.project.rcoffee.data.model.Banner
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.Category
import datlowashere.project.rcoffee.data.model.LoginResponse
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.data.model.Users
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //Banner
    @GET(AppConstant.GET_BANNER)
    suspend fun getBanners(): List<Banner>
    //Category

    @GET(AppConstant.GET_CATEGORY)
    suspend fun getCategories(): List<Category>
    //Product
    @GET(AppConstant.GET_PRODUCT)
    suspend fun getProducts(@Path("email_user") email_user: String): List<Product>
    //Rating

    @GET(AppConstant.GET_RATING)
    suspend fun getRatings(@Path("product_id") productId: Int): List<Rating>

    //Login, user
    @POST(AppConstant.LOGIN)
    suspend fun loginUser(@Body user: Users): Response<LoginResponse>

    @GET(AppConstant.GET_USER)
    fun getUser(@Path("email_user") email_user: String): Call<AuthResponse>
    //Basket
    @GET(AppConstant.GET_BASKET)
    suspend fun getBaskets(@Path("email_user") email_user: String): List<Basket>

    @POST(AppConstant.INSERT_BASKET)
    fun insertBasket(@Body basketItem: Basket): Call<Basket>

    @PUT(AppConstant.UPDATE_BASKET)
    fun updateBasket(@Path("basket_id") basketId: Int, @Body basketItem: Basket): Call<Basket>

    @DELETE(AppConstant.DELETE_BASKET)
    fun deleteBasket(@Path("basket_id") basketId: Int): Call<Void>
}
