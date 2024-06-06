package datlowashere.project.rcoffee.data.model

data class Product(
    val product_id: Int,
    val product_name: String,
    val img: String?,
    val description: String?,
    val price: Double,
    val category_id: Int,
    val average_rating: Double
)
