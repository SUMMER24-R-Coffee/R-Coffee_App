package datlowashere.project.rcoffee.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.databinding.LayoutItemProductBinding
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import datlowashere.project.rcoffee.utils.FormatterHelper

class ProductAdapter(
    private val products: List<Product>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION


    inner class ProductViewHolder(val binding: LayoutItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = LayoutItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.binding.apply {
            tvProductName.text = product.product_name
            tvPriceItemProduct.text = FormatterHelper.formatCurrency(product.price)
            val rating = product.average_rating
            tvStarItemProduct.text = String.format("%.1f", rating)
            tvDescItemProduct.text = product.description
            Glide.with(imgProductItemImg.context)
                .load(product.img)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(imgProductItemImg)

            if (product.favorite_id!=null) {
                imgFavProduct.setImageResource(R.drawable.red_heart)
            } else {
                imgFavProduct.setImageResource(R.drawable.hert)
            }
            updateFavoriteIcon(product.favorite_id != null, this)

            root.isSelected = selectedPosition == position
        }

        holder.binding.imgProductItemImg.setOnClickListener {
            notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            itemClickListener.onProductClick(product)
        }

        holder.binding.btnAddToBasket.setOnClickListener {
            itemClickListener.onAddBasketClick(product)
        }
        holder.binding.imgFavProduct.setOnClickListener {
            val isCurrentlyFavorite = product.favorite_id != null
            val newFavoriteStatus = !isCurrentlyFavorite
            updateFavoriteIcon(newFavoriteStatus, holder.binding)
            product.favorite_id = if (newFavoriteStatus) 1 else null
            itemClickListener.onFavoriteClick(product)
        }
    }

    override fun getItemCount(): Int = products.size
    private fun updateFavoriteIcon(isFavorite: Boolean, binding: LayoutItemProductBinding) {
        if (isFavorite) {
            binding.imgFavProduct.setImageResource(R.drawable.red_heart)
        } else {
            binding.imgFavProduct.setImageResource(R.drawable.hert)
        }
    }
    interface OnItemClickListener {
        fun onProductClick(product: Product)
        fun onAddBasketClick(product: Product)

        fun onFavoriteClick(product: Product)
    }
}
