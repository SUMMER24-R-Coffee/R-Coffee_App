package datlowashere.project.rcoffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.databinding.LayoutItemProductBinding
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
            tvStarItemProduct.text = product.average_rating.toString()
            tvDescItemProduct.text = product.description
            Glide.with(imgProductItemImg.context)
                .load(product.img)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(imgProductItemImg)

            root.isSelected = selectedPosition == position
        }

        holder.binding.imgProductItemImg.setOnClickListener {
            notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            itemClickListener.onProductClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    interface OnItemClickListener {
        fun onProductClick(product: Product)
    }
}
