package datlowashere.project.rcoffee.constant

class AppConstant {
     val BASE_URL = "http://192.168.1.5:3000/api-app/"

    companion object {
        const val GET_BANNER = "banner"
        const val GET_CATEGORY ="categories"
        const val GET_PRODUCT = "products"
        const val GET_RATING ="ratings/{product_id}"
    }
}