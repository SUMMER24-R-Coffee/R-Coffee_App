package datlowashere.project.rcoffee.data.model

data class Basket(
    val basket_id: Int = 0,
    val categoryName: String = "",
    val name: String = "",
    val img: String = "",
    val price: Double = 0.0,
    var quantity: Int = 0,
    val product_id: Int = 0,
    val email_user: String = "",
    val order_id: String? = null
)
