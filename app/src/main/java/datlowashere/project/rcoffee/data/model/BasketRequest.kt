package datlowashere.project.rcoffee.data.model

data class BasketRequest(
    val order_id: String,
    val basket_id: List<Int>
)