package datlowashere.project.rcoffee.ui.view.activity.notification

import android.os.Bundle
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
        notificationViewModel = ViewModelProvider(this, factory).get(NotificationViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        binding.btnBackNotifications.setOnClickListener { finish() }
        notificationViewModel.getNotifications(getEmail())
        //TODO: FIX LAYOUT ITEM NOTIFICATION, REVIEW, REPOSITORY, DELETE, MARK AS REAS
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(
            notifications = listOf(),
            onDeleteClick = { notification ->handleDeleteClick(notification)
            },
            onReadClick = { notification ->handleOnReadClick(notification)
            },
        )
        binding.rcvNotification.layoutManager = LinearLayoutManager(this)
        binding.rcvNotification.adapter = notificationAdapter
    }

    private fun observeViewModel() {
        notificationViewModel.notifications.observe(this) { notifications ->
            notificationAdapter.updateNotifications(notifications)
            binding.tvMessageNotification.visibility = if (notifications.isEmpty()) View.VISIBLE else View.GONE
        }

        notificationViewModel.notificationReadStatus.observe(this) { success ->
            if (success) {
                Toast.makeText(this,"Mask at read successful", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Failed mask as read", Toast.LENGTH_SHORT).show()

            }
        }

        notificationViewModel.notificationDeleteStatus.observe(this) { success ->
            if (success) {
                Toast.makeText(this,"Notification has been deleted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Failed deleting", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun handleOnReadClick(notification: Notification){
//        notificationViewModel.markNotificationAsRead(notification.notification_id)
//        notificationViewModel.getNotifications(getEmail())

    }
    private fun handleDeleteClick(notification: Notification){
        notificationViewModel.deleteNotification(notification.notification_id)
        notificationViewModel.getNotifications(getEmail())
    }
    private fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: ""
    }
}
