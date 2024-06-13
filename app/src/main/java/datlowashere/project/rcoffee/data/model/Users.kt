package datlowashere.project.rcoffee.data.model

data class Users(
    val email_user: String,
    val password: String,
    val gender: String? = null,
    val phone: String? = null,
    val user_img: String? = null,
    val name: String? = null
)

