package datlowashere.project.rcoffee.constant

class AppConstant {
     val BASE_URL = "http://192.168.1.5:3000/api-app/"

    companion object {
        const val GET_BANNER = "banner"
        const val GET_CATEGORY ="categories"
        const val GET_PRODUCT = "products/{email_user}"
        const val GET_RATING ="ratings/{product_id}"
        const val LOGIN ="login"
        const val GET_USER ="user/{email_user}"
        const val GET_BASKET="baskets/{email_user}"
        const val ADD_TO_BASKET="add-to-basket"
        const val UPDATE_BASKET ="update-basket/{basket_id}"
        const val UPDATE_TO_BASKET="update-to-basket"
        const val DELETE_BASKET="delete-basket/{basket_id}"
        const val GET_ADDRESS="addresses/{email_user}"
        const val ADD_ADDRESS="insert-address"
        const val GET_VOUCHER="vouchers"
        const val ORDER="insert-order"
        const val UPDATE_ORD_ID="update-order-id-basket"

    }
}