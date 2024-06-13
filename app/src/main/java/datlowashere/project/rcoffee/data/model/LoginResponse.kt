package datlowashere.project.rcoffee.data.model

data class LoginResponse (
    val status: String,
    val message: String,
    val users: Users?
)