package datlowashere.project.rcoffee.data.network

import datlowashere.project.rcoffee.constant.AppConstant
import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.data.model.ApiResponse
import datlowashere.project.rcoffee.data.model.AuthResponse
import datlowashere.project.rcoffee.data.model.Banner
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.BasketRequest
import datlowashere.project.rcoffee.data.model.Category
import datlowashere.project.rcoffee.data.model.LoginResponse
import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.data.model.Users
import datlowashere.project.rcoffee.data.model.Voucher
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @POST(AppConstant.ADD_TO_BASKET)
    suspend fun addToBasket(@Body Basket: Basket): Basket

    @POST(AppConstant.UPDATE_TO_BASKET)
    suspend fun updateToBasket(@Body basket: Basket): Basket

    @PUT(AppConstant.UPDATE_BASKET)
    fun updateQuanty(@Path("basket_id") basketId: Int, @Body basket: Basket): Call<ApiResponse>

    @DELETE(AppConstant.DELETE_BASKET)
    fun deleteBasket(@Path("basket_id") basketId: Int): Call<Void>
    @PUT(AppConstant.UPDATE_ORD_ID)
    fun updateOrderIDBasket(@Body basketRequest: BasketRequest): Call<Void>


    //address
    @GET(AppConstant.GET_ADDRESS)
    fun getAddresses(@Path("email_user") emailUser: String): Call<List<Address>>

    @POST(AppConstant.ADD_ADDRESS)
    fun addAddress(@Body address: Address): Call<Address>

    @GET(AppConstant.GET_VOUCHER)
    fun getVoucher(): Call<List<Voucher>>

    @POST(AppConstant.ORDER)
    fun orderItem(@Body order: Order): Call<Order>

}
