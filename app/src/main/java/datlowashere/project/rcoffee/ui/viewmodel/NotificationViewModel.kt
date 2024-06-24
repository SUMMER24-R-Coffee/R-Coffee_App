package datlowashere.project.rcoffee.ui.viewmodel

import androidx.lifecycle.*
import datlowashere.project.rcoffee.data.model.Notification
import datlowashere.project.rcoffee.data.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NotificationViewModel(private val notificationRepository: NotificationRepository) : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> get() = _notifications

    private val _markAsReadResult = MutableLiveData<Result<Void>>()
    val markAsReadResult: LiveData<Result<Void>> get() = _markAsReadResult

    private val _deleteNotificationResult = MutableLiveData<Result<Void>>()
    val deleteNotificationResult: LiveData<Result<Void>> get() = _deleteNotificationResult

    fun getNotifications(emailUser: String) {
        viewModelScope.launch {
            try {
                val notificationsList = notificationRepository.getNotifications(emailUser)
                _notifications.postValue(notificationsList)
            } catch (e: Exception) {
                e.printStackTrace()
                _notifications.postValue(emptyList())
            }
        }
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<Void> = notificationRepository.markAsRead(notificationId)
                if (response.isSuccessful) {
                    _markAsReadResult.postValue(Result.success(response.body()) as Result<Void>?)
                } else {
                    _markAsReadResult.postValue(Result.failure(Exception("Failed to mark notification as read")))
                }
            } catch (e: Exception) {
                _markAsReadResult.postValue(Result.failure(e))
            }
        }
    }

    fun deleteNotification(notificationId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<Void> = notificationRepository.deleteNotification(notificationId)
                if (response.isSuccessful) {
                    _deleteNotificationResult.postValue(Result.success(response.body()) as Result<Void>?)
                } else {
                    _deleteNotificationResult.postValue(Result.failure(Exception("Failed to delete notification")))
                }
            } catch (e: Exception) {
                _deleteNotificationResult.postValue(Result.failure(e))
            }
        }
    }
    fun getUnreadNotificationCount(emailUser: String, onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val count = notificationRepository.getUnreadNotificationCount(emailUser)
                onSuccess(count)
            } catch (e: Exception) {
                onError(e.message ?: "An error occurred")
            }
        }
    }
}

class NotificationViewModelFactory(private val notificationRepository: NotificationRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(notificationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
