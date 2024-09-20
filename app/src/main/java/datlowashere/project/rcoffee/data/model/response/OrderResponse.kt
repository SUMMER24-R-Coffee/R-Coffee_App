package datlowashere.project.rcoffee.data.model.response

data class OrderResponse(
    val status_order: String,
    val reason: String,
    val order_id: String,
    val email_user: String,
    val token: String
)