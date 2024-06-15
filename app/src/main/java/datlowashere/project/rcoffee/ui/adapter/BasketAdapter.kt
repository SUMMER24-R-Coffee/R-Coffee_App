package datlowashere.project.rcoffee.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.databinding.LayoutItemBasketBinding

class BasketAdapter(
    var baskets: List<Basket>,
    private val onItemClicked: (Basket) -> Unit,
    private val onCheckBoxClicked: (Basket) -> Unit,
    private val onEditClicked: (Basket) -> Unit,
    private val onQuantityChanged: (Basket, Int) -> Unit,
    private val onRemoveClicked: (Basket) -> Unit
) : RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {

    inner class BasketViewHolder(private val binding: LayoutItemBasketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(basket: Basket) {
            Glide.with(binding.imgProductBasketItem.context)
                .load(basket.img)
                .placeholder(R.drawable.img_default)
                .into(binding.imgProductBasketItem)

            binding.tvNameBasketItem.text = basket.name
            binding.tvPriceBasketProductItem.text = "${basket.price} đ"
            binding.tvQuantityBasketItem.text = basket.quantity.toString()

            binding.ckItemBasket.setOnCheckedChangeListener(null)  // Prevent recycling issue
            binding.ckItemBasket.isChecked = false  // Or set based on your logic
            binding.ckItemBasket.setOnCheckedChangeListener { _, _ -> onCheckBoxClicked(basket) }

            binding.tvEditBasketItem.setOnClickListener {
                onEditClicked(basket)
            }

            binding.btnMinusBasket.setOnClickListener {
                val newQuantity = basket.quantity - 1
                if (newQuantity > 0) {  // Ensure quantity does not go below 1
                    onQuantityChanged(basket, newQuantity)
                }
            }

            binding.btnPlusBasket.setOnClickListener {
                val newQuantity = basket.quantity + 1
                onQuantityChanged(basket, newQuantity)
            }

            binding.btnRemoveBasketItem.setOnClickListener { onRemoveClicked(basket) }

            binding.root.setOnClickListener { onItemClicked(basket) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val binding = LayoutItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BasketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        holder.bind(baskets[position])
    }

    override fun getItemCount(): Int = baskets.size

    fun updateBaskets(newBaskets: List<Basket>) {
        baskets = newBaskets
        notifyDataSetChanged()
    }
}
