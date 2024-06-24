package datlowashere.project.rcoffee.constant

class AppConstant {
     val BASE_URL = "http://192.168.1.9:3000/api-app/"

    companion object {
        const val REQUEST_CODE_ADDRESS = 1
        const val REQUEST_CODE_VOUCHER = 2
        const val GET_BANNER = "banner"
        const val GET_CATEGORY ="categories"
        const val GET_PRODUCT = "products/{email_user}"
        const val GET_RATING ="ratings/{product_id}"
        const val LOGIN ="login"
        const val GET_USER ="user/{email_user}"
        const val GET_BASKET_ORD="basket/{order_id}"
        const val GET_BASKET="baskets/{email_user}"
        const val ADD_TO_BASKET="add-to-basket"
        const val UPDATE_BASKET ="update-basket/{basket_id}"
        const val UPDATE_TO_BASKET="update-to-basket"
        const val DELETE_BASKET="delete-basket/{basket_id}"
        const val GET_ADDRESS="addresses/{email_user}"
        const val ADD_ADDRESS="insert-address"
        const val GET_VOUCHER="vouchers"
        const val ORDER="insert-order"
        const val GET_ORDER="get-orders/{email_user}"
        const val UPDATE_STATUS_ORDER="update-status-order/{order_id}"
        const val UPDATE_ORD_ID="update-order-id-basket"
        const val INSERT_PAYMENT="insert-payment-detail"
        const val UPDATE_PAYMENT_STATUS="update-payment/{order_id}"
        const val IN_DEL_FAV="in-del-favorite"
        const val UPDATE_TOKEN_USER="update-token/{email_user}"
        const val GET_NOTIFICATION="notifications/{email_user}"
        const val UPDATE_NOTIFICATION="notification/{notification_id}/read"
        const val DELETE_NOTIFICATION="notification/{notification_id}/delete"

    }
}