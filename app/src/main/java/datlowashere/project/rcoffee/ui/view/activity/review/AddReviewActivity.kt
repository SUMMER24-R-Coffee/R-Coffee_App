package datlowashere.project.rcoffee.ui.view.activity.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.model.Rating
import datlowashere.project.rcoffee.data.repository.RatingRepository
import datlowashere.project.rcoffee.databinding.ActivityAddReviewBinding
import datlowashere.project.rcoffee.ui.adapter.AddRatingAdapter
import datlowashere.project.rcoffee.ui.viewmodel.RatingViewModel
import datlowashere.project.rcoffee.ui.viewmodel.RatingViewModelFactory
import datlowashere.project.rcoffee.utils.Resource
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class AddReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var addRatingAdapter: AddRatingAdapter
    private lateinit var ratingViewModel: RatingViewModel
    private var basketIds: List<Int> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
        setUpObservers()
        binding.btnBackRating.setOnClickListener { finish() }

        val baskets: List<Basket>? = intent.getParcelableArrayListExtra("BASKETS")
        basketIds = baskets?.map { it.basket_id } ?: emptyList()


        fetchRatingsForBaskets(basketIds)

        addRatingAdapter = AddRatingAdapter(baskets ?: emptyList(), emptyMap()) { basketId, productId, ratingValue, reviewText ->
            if (ratingValue < 1 || reviewText.isEmpty()) {
                Toast.makeText(this, "Please, write your review and rating first", Toast.LENGTH_SHORT).show()
            } else {
                val newRating = Rating(
                    basket_id = basketId,
                    product_id = productId,
                    rating = ratingValue,
                    review = reviewText,
                    email_user = getEmail()
                )
                ratingViewModel.insertRating(newRating)
            }
        }

        binding.rcvRating.layoutManager = LinearLayoutManager(this)
        binding.rcvRating.adapter = addRatingAdapter
    }

    private fun setUpViewModel() {
        val repository = RatingRepository()
        val factory = RatingViewModelFactory(repository)
        ratingViewModel = ViewModelProvider(this, factory).get(RatingViewModel::class.java)
    }

    private fun setUpObservers() {
        ratingViewModel.ratings.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { ratings ->
                        val ratingsMap = ratings.associateBy { it.basket_id }
                        addRatingAdapter.updateRatings(ratingsMap)
                        Log.d("AddRating", ratingsMap.toString())
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this, "Error fetching ratings", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                }
            }
        }
        ratingViewModel.insertRatingResult.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    Toast.makeText(this, "Your rating has been added", Toast.LENGTH_SHORT).show()
                    fetchRatingsForBaskets(basketIds)
                }
                is Resource.Error -> {
                    Toast.makeText(this, "Error adding rating", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun fetchRatingsForBaskets(basketIds: List<Int>) {
        ratingViewModel.getRatingsByBasketIds(basketIds)
    }

    private fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: " "
    }
}
