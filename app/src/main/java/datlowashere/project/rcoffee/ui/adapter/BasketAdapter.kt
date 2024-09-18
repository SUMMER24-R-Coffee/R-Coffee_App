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
    private val onTotalAmountChanged: (Double) -> Unit,
    private val context: Context

) : RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {
    private val selectedBaskets = mutableMapOf<Int, Basket>()

    inner class BasketViewHolder(private val binding: LayoutItemBasketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(basket: Basket) {
            Glide.with(binding.imgProductBasketItem.context)
                .load(basket.img)
                .placeholder(R.drawable.img_default)
                .into(binding.imgProductBasketItem)

            binding.tvNameBasketItem.text = basket.name
            binding.tvPriceBasketProductItem.text = FormatterHelper.formatCurrency(basket.price)
            binding.tvQuantityBasketItem.text = basket.quantity.toString()

            binding.ckItemBasket.setOnCheckedChangeListener(null)
            binding.ckItemBasket.isChecked = selectedBaskets.contains(basket.basket_id)
            binding.ckItemBasket.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedBaskets[basket.basket_id] = basket
                } else {
                    selectedBaskets.remove(basket.basket_id)
                }
                onCheckBoxClicked(basket)
                calculateTotalAmount()
            }


            binding.btnMinusBasket.setOnClickListener {
                val currentQuantity = binding.tvQuantityBasketItem.text.toString().toInt()
                val newQuantity = currentQuantity - 1

                if (newQuantity > 0) {
                    binding.tvQuantityBasketItem.text = newQuantity.toString()
                    onQuantityChanged(basket, newQuantity)
                    updateSelectedBasketQuantity(basket, newQuantity)

                }
                if (newQuantity < 1) {
                    showDeleteConfirmationDialog(basket)
                }
                calculateTotalAmount()

            }

            binding.btnPlusBasket.setOnClickListener {
                val currentQuantity = binding.tvQuantityBasketItem.text.toString().toInt()
                val newQuantity = currentQuantity + 1

                binding.tvQuantityBasketItem.text = newQuantity.toString()
                onQuantityChanged(basket, newQuantity)
                updateSelectedBasketQuantity(basket, newQuantity)
                calculateTotalAmount()

            }
            binding.btnRemoveBasketItem.setOnClickListener {
                showDeleteConfirmationDialog(basket)
            }

            binding.root.setOnClickListener { onItemClicked(basket) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val binding =
            LayoutItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                selectedBaskets.remove(basket.basket_id)
                calculateTotalAmount()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun updateSelectedBasketQuantity(basket: Basket, newQuantity: Int) {
        if (selectedBaskets.containsKey(basket.basket_id)) {
            selectedBaskets[basket.basket_id] = basket.copy(quantity = newQuantity)
        }
    }

    private fun calculateTotalAmount() {
        var totalAmount = 0.0
        for (basket in selectedBaskets.values) {
            totalAmount += basket.price * basket.quantity
        }
        onTotalAmountChanged(totalAmount)
    }

    fun getSelectedBaskets(): List<Basket> {
        return selectedBaskets.values.toList()
    }

    fun getTotalAmount(): Double {
        return selectedBaskets.values.sumByDouble { it.price * it.quantity }
    }

}
