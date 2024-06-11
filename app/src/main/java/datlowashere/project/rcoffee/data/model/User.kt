package datlowashere.project.rcoffee.data.model

data class User(
    val emailUser: String,
    val password: String,
    val gender: String? = null,
    val phone: String? = null,
    val userImg: String? = null,
    val name: String? = null
)