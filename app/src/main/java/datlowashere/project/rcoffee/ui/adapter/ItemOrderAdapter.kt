package datlowashere.project.rcoffee.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.databinding.LayoutItemOrderBinding
import datlowashere.project.rcoffee.utils.FormatterHelper

class ItemOrderAdapter(
    private val context: Context,
    private val baskets: List<Basket>
) : RecyclerView.Adapter<ItemOrderAdapter.ItemOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemOrderViewHolder {
        val binding = LayoutItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemOrderViewHolder, position: Int) {
        val basket = baskets[position]
        holder.bind(basket)
    }

    override fun getItemCount(): Int {
        return baskets.size
    }

    inner class ItemOrderViewHolder(private val binding: LayoutItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(basket: Basket) {
            Glide.with(context)
                .load(basket.img)
                .placeholder(R.drawable.img_default)
                .into(binding.imgProductOrderItem)

            binding.tvNameOrderItem.text = basket.name
            binding.tvPriceOrderProductItem.text = FormatterHelper.formatCurrency(basket.price)
            binding.tvQuantityOrderProductItem.text = "X"+basket.quantity.toString()


        }
    }
}
