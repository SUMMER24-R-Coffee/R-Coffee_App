package datlowashere.project.rcoffee.ui.view.activity.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.data.model.Notification
import datlowashere.project.rcoffee.data.repository.NotificationRepository
import datlowashere.project.rcoffee.databinding.ActivityNotificationBinding
import datlowashere.project.rcoffee.ui.adapter.NotificationAdapter
import datlowashere.project.rcoffee.ui.viewmodel.NotificationViewModel
import datlowashere.project.rcoffee.ui.viewmodel.NotificationViewModelFactory
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NotificationRepository()
        val factory = NotificationViewModelFactory(repository)
        notificationViewModel =
            ViewModelProvider(this, factory).get(NotificationViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        binding.btnBackNotifications.setOnClickListener { finish() }
        notificationViewModel.getNotifications(getEmail())
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(
            notifications = listOf(),
            onDeleteClick = { notification -> handleDeleteClick(notification) },
            onReadClick = { notification -> handleOnReadClick(notification) }
        )
        binding.rcvNotification.layoutManager = LinearLayoutManager(this)
        binding.rcvNotification.adapter = notificationAdapter
    }

    private fun observeViewModel() {
        notificationViewModel.notifications.observe(this) { notifications ->
            notificationAdapter.updateNotifications(notifications)
            binding.tvMessageNotification.visibility =
                if (notifications.isEmpty()) View.VISIBLE else View.GONE
        }

        notificationViewModel.markAsReadResult.observe(this) { result ->
            result.onSuccess {
                Log.d("NotificationActivity", "Masked as read successful")
                notificationViewModel.getNotifications(getEmail())
            }.onFailure {
                Log.d("NotificationActivity", "Masked as read failed")
            }
        }

        notificationViewModel.deleteNotificationResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Notification has been deleted", Toast.LENGTH_SHORT).show()
                notificationViewModel.getNotifications(getEmail())
            }.onFailure {
                Toast.makeText(this, "Failed to delete notification", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleOnReadClick(notification: Notification) {
        notificationViewModel.markAsRead(notification.notification_id)
    }

    private fun handleDeleteClick(notification: Notification) {
        notificationViewModel.deleteNotification(notification.notification_id)
    }

    private fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: ""
    }

}
