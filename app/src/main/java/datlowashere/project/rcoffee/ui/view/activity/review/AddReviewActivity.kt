package datlowashere.project.rcoffee.ui.view.activity.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.databinding.ActivityAddReviewBinding
import datlowashere.project.rcoffee.ui.adapter.AddRatingAdapter

class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var addRatingAdapter: AddRatingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackRating.setOnClickListener { finish() }

        val baskets: List<Basket>? = intent.getParcelableArrayListExtra("BASKETS")

        addRatingAdapter = AddRatingAdapter(baskets ?: emptyList()) { productId, ratingValue, reviewText ->

        }
        binding.rcvRating.layoutManager = LinearLayoutManager(this)
        binding.rcvRating.adapter = addRatingAdapter
    }
}