package datlowashere.project.rcoffee.ui.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.databinding.LayoutItemOrderBinding
import datlowashere.project.rcoffee.utils.FormatterHelper

class OrderItemAdapter(
    private var orders: List<Order>,
    private val onItemClicked: (Order) -> Unit,
) : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {

    inner class OrderItemViewHolder(private val binding: LayoutItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Order) {
            Glide.with(binding.imgProductOrderItem.context)
                .load(order.img)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(binding.imgProductOrderItem)

            binding.tvNameOrderItem.text = order.name
            binding.tvPriceOrderProductItem.text =
                FormatterHelper.formatCurrency(order.total_amount)
            binding.tvQuantityOrderProductItem.text = "x" + order.total_item.toString()
            binding.tvTotalItem.text = "Detail"
            binding.tvStatusOrderItem.text = order.status_order

            when (order.status_order.toLowerCase()) {
                "cancelled" -> binding.tvStatusOrderItem.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.red_exp
                    )
                )

                "delivered" -> binding.tvStatusOrderItem.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.green_ext
                    )
                )

                "delivering" -> binding.tvStatusOrderItem.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.blue_bld
                    )
                )

                else -> binding.tvStatusOrderItem.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.yellow_erth
                    )
                )
            }
            binding.tvTimeOrderItem.text =
                order.create_at?.let { FormatterHelper.formatDateTime(it) }

            binding.root.setOnClickListener {
                onItemClicked(order)
            }

            binding.cardViewItemOrder.setOnClickListener {
                onItemClicked(order)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding =
            LayoutItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }


}
