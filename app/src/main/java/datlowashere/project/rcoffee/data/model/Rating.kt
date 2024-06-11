package datlowashere.project.rcoffee.data.model

import java.util.*

data class Rating(
    val rating_id: Int,
    val rating: Int,
    val review: String?,
    val product_id: Int?,
    val email_user: String?,
    val create_at: Date,
    val user_name: String?,
    val user_img: String?
)
