package datlowashere.project.rcoffee.ui.view.activity.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.ActivityProductInformationBinding
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.repository.ProductRepository
import datlowashere.project.rcoffee.ui.adapter.RatingAdapter
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModel
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModelFactory
import datlowashere.project.rcoffee.utils.FormatterHelper
import datlowashere.project.rcoffee.utils.Resource

class ProductInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductInformationBinding
    private lateinit var ratingAdapter: RatingAdapter
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackPrdIm.setOnClickListener{
            finish()
        }

        val repository = ProductRepository()
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        val product: Product? = intent.getParcelableExtra("product")

        product?.let {
            binding.tvProductNamInf.text = it.product_name
            binding.tvDescriptionPrdInf.text = it.description
            binding.tvProductPriceInf.text = FormatterHelper.formatCurrency(it.price)
            binding.tvAverageStarInf.text =String.format("%.1f", it.average_rating)

            Glide.with(this)
                .load(it.img)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(binding.imgProductInf)

            productViewModel.getRatings(it.product_id)

            productViewModel.ratings.observe(this, { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { ratings ->
                            if (ratings.isNotEmpty()) {
                                binding.tvViewAllReview.visibility = View.VISIBLE
                                ratingAdapter = RatingAdapter(ratings)
                                binding.rcvReviews.apply {
                                    layoutManager = LinearLayoutManager(this@ProductInformationActivity)
                                    adapter = ratingAdapter
                                }
                            } else {
                                binding.tvViewAllReview.visibility = View.GONE
                            }
                        }
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            })

            handleValueQuantityWithPrice(it.price)
        }
    }

    private fun handleValueQuantityWithPrice(productPrice: Double) {
        var quantity = 0
        binding.tvQuantity.text = quantity.toString()

        binding.btnUpdateBasket.isEnabled = false
        updateBasketButtonText(quantity, productPrice)

        binding.btnIncreasePrd.setOnClickListener {
            if (quantity < 20) {
                quantity++
                binding.tvQuantity.text = quantity.toString()
                binding.btnUpdateBasket.isEnabled = quantity > 0
                updateBasketButtonText(quantity, productPrice)
            } else {
                Toast.makeText(this, "Maximum is 20", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMinusPrd.setOnClickListener {
            if (quantity > 0) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
                binding.btnUpdateBasket.isEnabled = quantity > 0
                updateBasketButtonText(quantity, productPrice)
            }

            if (quantity < 1) {
                Toast.makeText(this, "Minimum is 1", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateBasketButtonText(quantity: Int, productPrice: Double) {
        val totalPrice = quantity * productPrice
        binding.btnUpdateBasket.text = "Update Basket - "+FormatterHelper.formatCurrency(totalPrice)
    }


}