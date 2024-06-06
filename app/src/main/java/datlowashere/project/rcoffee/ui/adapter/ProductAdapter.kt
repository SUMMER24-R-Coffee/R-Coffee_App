package datlowashere.project.rcoffee.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.databinding.LayoutItemProductBinding
import datlowashere.project.rcoffee.utils.FormatterHelper

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

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
            tvStarItemProduct.text =product.average_rating.toString()
            tvDescItemProduct.text = product.description
            Glide.with(imgProductItemImg.context)
                .load(product.img)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(imgProductItemImg)
        }
    }

    override fun getItemCount() = products.size
}
