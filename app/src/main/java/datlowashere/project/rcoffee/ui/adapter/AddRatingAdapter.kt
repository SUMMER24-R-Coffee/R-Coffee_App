package datlowashere.project.rcoffee.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.databinding.LayoutItemRatingBinding
import datlowashere.project.rcoffee.utils.FormatterHelper

class AddRatingAdapter(
    private val baskets: List<Basket>,
    private var ratingsMap: Map<Int, Rating?>,
    private val onRatingSaved: (Int, Int, Int, String) -> Unit
) : RecyclerView.Adapter<AddRatingAdapter.RatingViewHolder>() {

    inner class RatingViewHolder(private val binding: LayoutItemRatingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(basket: Basket, rating: Rating?) {
            binding.apply {
                Glide.with(imgProductRating.context)
                    .load(basket.img)
                    .centerCrop()
                    .into(imgProductRating)
                tvNamePrRating.text = basket.name
                tvPricePrRating.text = FormatterHelper.formatCurrency(basket.price)

                if (rating != null) {
                    btnSaveRating.visibility = View.GONE
                    tvTimeRating.text = rating.create_at?.let { FormatterHelper.formatTme(it) }
                    edContentRating.setText(rating.review)
                    edContentRating.isEnabled = false
                    ratingBar.setIsIndicator(true)
                    ratingBar.rating = rating.rating.toFloat()
                } else {
                    btnSaveRating.visibility = View.VISIBLE
                    tvTimeRating.text = ""
                    edContentRating.setText("")
                    edContentRating.isEnabled = true
                    ratingBar.isEnabled = true
                    ratingBar.rating = 0.0f
                }

                btnSaveRating.setOnClickListener {
                    val ratingValue = ratingBar.rating.toInt()
                    val reviewText = edContentRating.text.toString()
                    onRatingSaved(basket.basket_id, basket.product_id, ratingValue, reviewText)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val binding = LayoutItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val basket = baskets[position]
        val rating = ratingsMap[basket.basket_id]
        holder.bind(basket, rating)
    }

    override fun getItemCount() = baskets.size

    fun updateRatings(newRatings: Map<Int, Rating?>) {
        ratingsMap = newRatings
        notifyDataSetChanged()
    }
}
