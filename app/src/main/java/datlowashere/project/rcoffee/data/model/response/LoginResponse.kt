package datlowashere.project.rcoffee.data.model.response

import datlowashere.project.rcoffee.data.model.Users

data class LoginResponse (
    val status: String,
    val message: String,
    val users: Users?
)