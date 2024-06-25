package datlowashere.project.rcoffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.databinding.LayoutItemRatingBinding

class AddRatingAdapter(
    private val baskets: List<Basket>,
    private val onRatingSaved: (Int, Int, String) -> Unit
) : RecyclerView.Adapter<AddRatingAdapter.RatingViewHolder>() {

    inner class RatingViewHolder(private val binding: LayoutItemRatingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(basket: Basket) {
            binding.apply {
                Glide.with(imgProductRating.context)
                    .load(basket.img)
                    .centerCrop()
                    .into(imgProductRating)
                tvNamePrRating.text = basket.name
                tvPricePrRating.text = basket.price.toString()

                btnSaveRating.setOnClickListener {
                    val ratingValue = ratingBar.rating.toInt()
                    val reviewText = edContentRating.text.toString()
                    onRatingSaved(basket.product_id, ratingValue, reviewText)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val binding = LayoutItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.bind(baskets[position])
    }

    override fun getItemCount() = baskets.size
}
