package datlowashere.project.rcoffee.data.repository

import android.util.Log
import datlowashere.project.rcoffee.data.model.Notification
import datlowashere.project.rcoffee.data.network.ApiClient
import datlowashere.project.rcoffee.utils.Resource
import retrofit2.Response

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
    suspend fun markAsRead(notificationId: Int): Response<Void> {
        return apiService.markAsReadNotification(notificationId)
    }

    suspend fun deleteNotification(notificationId: Int): Response<Void> {
        return apiService.deleteNotification(notificationId)
    }
    suspend fun getUnreadNotificationCount(emailUser: String): Int {
        return try {
            val response = apiService.getUnreadNotificationCount(emailUser)
            response.firstOrNull()?.total_remain ?: 0
        } catch (e: Exception) {
            0
        }
    }
}
