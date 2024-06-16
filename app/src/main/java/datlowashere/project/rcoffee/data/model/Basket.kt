package datlowashere.project.rcoffee.data.model

data class Basket(
    val basket_id: Int = 0,
    val categoryName: String = "",
    val name: String = "",
    val img: String = "",
    val price: Int = 0,
    val quantity: Int,
    val productId: Int,
    val emailUser: String,
    val orderId: String? = null
)
