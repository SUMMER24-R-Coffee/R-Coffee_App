package datlowashere.project.rcoffee.data.model.response

import datlowashere.project.rcoffee.data.model.Users

data class AuthResponse(
    val status: String,
    val message: String,
    val users: List<Users>?
)
