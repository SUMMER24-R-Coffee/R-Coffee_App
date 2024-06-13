package datlowashere.project.rcoffee.data.model

data class AuthResponse(
    val status: String,
    val message: String,
    val users: List<Users>?
)
