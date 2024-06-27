package datlowashere.project.rcoffee.data.network

import datlowashere.project.rcoffee.constant.AppConstant
import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.data.model.response.ApiResponse
import datlowashere.project.rcoffee.data.model.response.AuthResponse
import datlowashere.project.rcoffee.data.model.Banner
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.BasketRequest
import datlowashere.project.rcoffee.data.model.Category
import datlowashere.project.rcoffee.data.model.Favorite
import datlowashere.project.rcoffee.data.model.Notification
import datlowashere.project.rcoffee.data.model.response.LoginResponse
import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.data.model.PaymentDetail
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.data.model.Users
import datlowashere.project.rcoffee.data.model.Voucher
import datlowashere.project.rcoffee.data.model.response.OrderResponse
import datlowashere.project.rcoffee.data.model.response.RegisterResponese
import datlowashere.project.rcoffee.data.model.response.TokenResponse
import datlowashere.project.rcoffee.data.model.response.TotalUnread
import datlowashere.project.rcoffee.utils.Resource
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
    @GET(AppConstant.GET_RATING_BASKET)
    suspend fun getRatingsByBasketIds(@Query("basket_ids") basketIds: String): List<Rating>
    @POST(AppConstant.INSERT_RATING)
    suspend fun insertRating(@Body rating: Rating): Rating
    //Login, register user
    @POST(AppConstant.GET_VERTIFICATION)
    suspend fun requestCode(@Body registerResponese: RegisterResponese): Response<ApiResponse>

    @POST(AppConstant.REGISTER)
    suspend fun registerUser(@Body registerResponese: RegisterResponese): Response<ApiResponse>

    @POST(AppConstant.LOGIN)
    suspend fun loginUser(@Body user: Users): Response<LoginResponse>

    @PUT(AppConstant.UPDATE_TOKEN_USER)
    suspend fun updateTokenUser(@Path("email_user")emailUser: String, @Body tokenResponse: TokenResponse): Call<Void>


    @GET(AppConstant.GET_USER)
    fun getUser(@Path("email_user") email_user: String): Call<AuthResponse>
    //Basket
    @GET(AppConstant.GET_BASKET)
    suspend fun getBaskets(@Path("email_user") email_user: String): List<Basket>

    @GET(AppConstant.GET_BASKET_ORD)
    fun getBasketByOrderId(@Path("order_id") orderId: String): Call<List<Basket>>
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

    //order
    @POST(AppConstant.ORDER)
    fun orderItem(@Body order: Order): Call<Order>
    @GET(AppConstant.GET_ORDER)
    fun getOrders(@Path("email_user") emailUser: String): Call<List<Order>>

    @PUT(AppConstant.UPDATE_STATUS_ORDER)
    fun updateStatusOrder(@Path("order_id") orderId: String, @Body order: OrderResponse): Call<Void>

    //payment
    @POST(AppConstant.INSERT_PAYMENT)
    fun insertPaymentDetail(@Body paymentDetail: PaymentDetail): Call<Void>
    @PUT(AppConstant.UPDATE_PAYMENT_STATUS)
    suspend fun updatePaymentDetail(@Path("order_id") orderId: String, @Body paymentDetail: PaymentDetail): Call<Void>

    //favorite
    @POST(AppConstant.IN_DEL_FAV)
    suspend fun insertOrDelFav(@Body favorite: Favorite): Favorite
    //notification
    @GET(AppConstant.GET_NOTIFICATION)
    suspend fun getNotification(@Path("email_user") emailUser: String): List<Notification>
    @PUT(AppConstant.UPDATE_NOTIFICATION)
    suspend fun markAsReadNotification(@Path("notification_id") notificationId: Int): Response<Void>
    @DELETE(AppConstant.DELETE_NOTIFICATION)
    suspend fun deleteNotification(@Path("notification_id") notificationId: Int): Response<Void>

    @GET(AppConstant.GET_UNREAD_NOTIFICATION)
    suspend fun getUnreadNotificationCount(@Path("email_user") emailUser: String): List<TotalUnread>

    //TODO: Fix update, delete basket

}
