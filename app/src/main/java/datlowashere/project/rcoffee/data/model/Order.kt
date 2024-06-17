package datlowashere.project.rcoffee.data.model

import java.util.*

data class Order(
    val order_id: String,
    val order_date: Date,
    val create_at: Date,
    val update_at: Date?,
    val payment_method: String?,
    val total_amount: Double,
    val discount_amount: Double?,
    val status_order: String,
    val address_id: Int?,
    val voucher_id: Int?,
    val order_message: String?
)
