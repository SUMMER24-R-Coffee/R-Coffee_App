package datlowashere.project.rcoffee.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Notification
import datlowashere.project.rcoffee.databinding.LayoutItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter(
    var notifications: List<Notification>,
    private val onDeleteClick: (Notification) -> Unit,
    private val onReadClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = LayoutItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    inner class NotificationViewHolder(private val binding: LayoutItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        fun bind(notification: Notification) {
            binding.tvTitleMessageNoti.text = notification.title
            binding.tvMessageNoti.text = notification.message
            binding.tvCreateNoti.text = dateFormatter.format(notification.create_at)

            if (notification.is_read == 1) {
                binding.lnContainerNotification.setBackgroundResource(R.color.white)
                binding.tvCheckMessage.visibility =View.VISIBLE
            } else {
                binding.lnContainerNotification.setBackgroundResource(R.color.light_pink)
                binding.tvCheckMessage.visibility =View.GONE
            }

            binding.imgProductNotifi.setOnClickListener {
                onReadClick(notification)
                binding.lnContainerNotification.setBackgroundResource(R.color.white)
            }

            binding.tvDeleteNotification.setOnClickListener {
                onDeleteClick(notification)
            }
        }
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun updateNotifications(newNotifications: List<Notification>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }
}
