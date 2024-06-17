package datlowashere.project.rcoffee.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.databinding.LayoutItemBasketBinding
import datlowashere.project.rcoffee.utils.FormatterHelper

class BasketAdapter(
    var baskets: List<Basket>,
    private val onItemClicked: (Basket) -> Unit,
    private val onCheckBoxClicked: (Basket) -> Unit,
    private val onQuantityChanged: (Basket, Int) -> Unit,
    private val onRemoveClicked: (Basket) -> Unit,
    private val context: Context

) : RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {

    inner class BasketViewHolder(private val binding: LayoutItemBasketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(basket: Basket) {
            Glide.with(binding.imgProductBasketItem.context)
                .load(basket.img)
                .placeholder(R.drawable.img_default)
                .into(binding.imgProductBasketItem)

            binding.tvNameBasketItem.text = basket.name
            binding.tvPriceBasketProductItem.text = FormatterHelper.formatCurrency(basket.price)
            binding.tvQuantityBasketItem.text = basket.quantity.toString()

            binding.ckItemBasket.setOnCheckedChangeListener(null)
            binding.ckItemBasket.isChecked = false
            binding.ckItemBasket.setOnCheckedChangeListener { _, _ -> onCheckBoxClicked(basket) }


            binding.btnMinusBasket.setOnClickListener {
                val currentQuantity = binding.tvQuantityBasketItem.text.toString().toInt()
                val newQuantity = currentQuantity - 1

                if (newQuantity > 0) {
                    binding.tvQuantityBasketItem.text = newQuantity.toString()
                    onQuantityChanged(basket, newQuantity)
                }
                if(newQuantity <1){
                    showDeleteConfirmationDialog(basket)
                }
            }


            binding.btnPlusBasket.setOnClickListener {
                val currentQuantity = binding.tvQuantityBasketItem.text.toString().toInt()
                val newQuantity = currentQuantity + 1

                binding.tvQuantityBasketItem.text = newQuantity.toString()
                onQuantityChanged(basket, newQuantity)
            }
            binding.btnRemoveBasketItem.setOnClickListener {
                showDeleteConfirmationDialog(basket)
            }

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

    private fun showDeleteConfirmationDialog(basket: Basket) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.apply {
            setMessage("Are you sure you want to delete this item?")
            setPositiveButton("Yes") { _, _ ->
                onRemoveClicked(basket)
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

}
