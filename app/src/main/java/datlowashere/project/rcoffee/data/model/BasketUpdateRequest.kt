package datlowashere.project.rcoffee.data.model

data class BasketUpdateRequest(
    val quantity: Int,
    val product_id: Int,
    val email_user: String
)