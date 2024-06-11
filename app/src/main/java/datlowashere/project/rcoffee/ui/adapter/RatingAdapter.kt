package datlowashere.project.rcoffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.databinding.LayoutItemCommentBinding

class RatingAdapter(
    private val ratings: List<Rating>
) : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    inner class RatingViewHolder(val binding: LayoutItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val binding = LayoutItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        val rating = ratings[position]
        holder.binding.apply {
            tvComment.text = rating.review ?: "No review provided"
            tvNameUserComment.text = rating.user_name ?: "Anonymous"
            Glide.with(imgUserComment.context)
                .load(rating.user_img)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(imgUserComment)

            lnStarComent.removeAllViews()

            val fullStars = rating.rating
            val halfStar = rating.rating % 1 != 0

            for (i in 1..fullStars) {
                val starImage = ImageView(holder.itemView.context)
                starImage.setImageResource(R.drawable.star)
                lnStarComent.addView(starImage)
            }

            if (halfStar) {
                val starImage = ImageView(holder.itemView.context)
                starImage.setImageResource(R.drawable.half_star)
                lnStarComent.addView(starImage)
            }
        }
    }

    override fun getItemCount(): Int = ratings.size
}
