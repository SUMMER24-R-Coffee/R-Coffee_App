package datlowashere.project.rcoffee.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.databinding.LayoutItemFavouriteBinding
import datlowashere.project.rcoffee.utils.FormatterHelper

class FavoriteProductAdapter(
    private var products: List<Product>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<FavoriteProductAdapter.FavoriteProductViewHolder>() {

    inner class FavoriteProductViewHolder(val binding: LayoutItemFavouriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductViewHolder {
        val binding = LayoutItemFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteProductViewHolder, position: Int) {
        val product = products[position]
        holder.binding.apply {
            tvNameFavoriteItem.text = product.product_name
            tvPriceFavoriteProductItem.text = FormatterHelper.formatCurrency(product.price)
            val rating = product.average_rating
            tvStarFavoriteItem.text = String.format("%.1f", rating)
            Glide.with(imgProductFavoriteItem.context)
                .load(product.img)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(imgProductFavoriteItem)

            updateFavoriteIcon(product.favorite_id != null, this)


            imgHeartFav.setOnClickListener {
                val isCurrentlyFavorite = product.favorite_id != null
                val newFavoriteStatus = !isCurrentlyFavorite
                updateFavoriteIcon(newFavoriteStatus, holder.binding)
                product.favorite_id = if (newFavoriteStatus) 1 else null
                itemClickListener.onFavoriteClick(product)
            }
            cardViewItemFav.setOnClickListener {
                itemClickListener.onFavoriteProductClick(product)
            }
        }
    }

    override fun getItemCount(): Int = products.size

    private fun updateFavoriteIcon(isFavorite: Boolean, binding: LayoutItemFavouriteBinding) {
        if (isFavorite) {
            binding.imgHeartFav.setImageResource(R.drawable.red_heart)
        } else {
            binding.imgHeartFav.setImageResource(R.drawable.hert)
        }
    }
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
    interface OnItemClickListener {
        fun onFavoriteClick(product: Product)
        fun onFavoriteProductClick(product: Product)
    }
}
