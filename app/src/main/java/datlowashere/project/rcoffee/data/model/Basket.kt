package datlowashere.project.rcoffee.data.model

data class Basket(
    val categoryName: String,
    val name: String,
    val img: String,
    val price: Int,
    val basketId: Int,
    val quantity: Int,
    val productId: Int,
    val emailUser: String,
    val orderId: String?
)