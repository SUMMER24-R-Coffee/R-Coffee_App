package datlowashere.project.rcoffee.data.model

import java.util.*

data class Order(
    val order_id: String,
    val order_date: Date? = null,
    val create_at: Date? = null,
    val update_at: Date? = null,
    val payment_method: String?,
    val total_amount: Double,
    val discount_amount: Double?,
    val status_order: String =" ",
    val address_id: Int?,
    val voucher_id: Int?,
    val order_message: String,
    val basket_id: List<Int>

)
