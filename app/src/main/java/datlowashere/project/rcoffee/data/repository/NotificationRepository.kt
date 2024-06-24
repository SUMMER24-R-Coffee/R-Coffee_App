package datlowashere.project.rcoffee.data.repository

import android.util.Log
import datlowashere.project.rcoffee.data.model.Notification
import datlowashere.project.rcoffee.data.network.ApiClient
class NotificationRepository {

    private val apiService = ApiClient.instance

    suspend fun getNotifications(emailUser: String): List<Notification> {
        return try {
            val notifications = apiService.getNotification(emailUser)
            Log.d("NotificationRepository", "Fetched notifications: $notifications")
            notifications
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Error fetching notifications", e)
            emptyList()
        }
    }
    suspend fun markAsRead(notificationId: Int): Boolean {
        return try {
            val response = apiService.markAsReadNotification(notificationId).execute()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteNotification(notificationId: Int): Boolean {
        return try {
            val response = apiService.deleteNotification(notificationId).execute()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
