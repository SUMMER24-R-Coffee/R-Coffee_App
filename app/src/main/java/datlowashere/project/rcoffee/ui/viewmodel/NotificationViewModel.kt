package datlowashere.project.rcoffee.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import datlowashere.project.rcoffee.data.model.Notification
import datlowashere.project.rcoffee.data.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(private val notificationRepository: NotificationRepository) : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> get() = _notifications

    private val _notificationReadStatus = MutableLiveData<Boolean>()
    val notificationReadStatus: LiveData<Boolean> get() = _notificationReadStatus

    private val _notificationDeleteStatus = MutableLiveData<Boolean>()
    val notificationDeleteStatus: LiveData<Boolean> get() = _notificationDeleteStatus

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

    fun markNotificationAsRead(notificationId: Int) {
        viewModelScope.launch {
            val success = notificationRepository.markAsRead(notificationId)
            _notificationReadStatus.postValue(success)
        }
    }

    fun deleteNotification(notificationId: Int) {
        viewModelScope.launch {
            val success = notificationRepository.deleteNotification(notificationId)
            _notificationDeleteStatus.postValue(success)
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
