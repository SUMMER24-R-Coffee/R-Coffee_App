package datlowashere.project.rcoffee.data.model

import java.util.*

data class Rating(
    val rating_id: Int=0,
    val rating: Int=0,
    val review: String="",
    val product_id: Int=0,
    val email_user: String="",
    val create_at: Date? =null,
    val user_name: String="",
    val user_img: String="",
    val basket_id: Int=0
)
