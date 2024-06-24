package datlowashere.project.rcoffee.data.model

import java.util.Date

data class Notification(
    val notification_id: Int,
    val title: String,
    val message: String?,
    val create_at: Date,
    val is_read: Int,
    val email_user: String?,
    val order_id: String?
)