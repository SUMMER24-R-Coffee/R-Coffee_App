package datlowashere.project.rcoffee.ui.view.activity.product

import android.content.Intent
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
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.data.repository.ProductRepository
import datlowashere.project.rcoffee.ui.adapter.RatingAdapter
import datlowashere.project.rcoffee.ui.component.DialogCustom
import datlowashere.project.rcoffee.ui.view.activity.basket.BasketActivity
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModel
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModelFactory
import datlowashere.project.rcoffee.utils.FormatterHelper
import datlowashere.project.rcoffee.utils.Resource
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class ProductInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductInformationBinding
    private lateinit var ratingAdapter: RatingAdapter
    private lateinit var productViewModel: ProductViewModel
    private lateinit var basketViewModel: BasketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackPrdIm.setOnClickListener {
            finish()
        }

        setUpViewModel()
        setupObservers()

        binding.btnUpdateBasket.setOnClickListener {
            setUpButtonUpdateBasket()
        }

        binding.btnBasketPrd.setOnClickListener {
            setUpButtonToBasket()
        }

    }

    private fun setUpViewModel() {
        val repository = ProductRepository()
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)

        val basketRepository = BasketRepository()
        val basketViewModelFactory = BasketViewModelFactory(basketRepository)
        basketViewModel =
            ViewModelProvider(this, basketViewModelFactory).get(BasketViewModel::class.java)
    }

    private fun setupObservers() {
        val product: Product? = intent.getParcelableExtra("product")
        product?.let {
            binding.apply {
                tvProductNamInf.text = it.product_name
                tvDescriptionPrdInf.text = it.description
                tvProductPriceInf.text = FormatterHelper.formatCurrency(it.price)
                tvAverageStarInf.text = String.format("%.1f", it.average_rating)

                binding.ratingBarProduct.rating = it.average_rating.toFloat()
                if (product.favorite_id != null) {
                    btnFavoritePrd.setImageResource(R.drawable.red_heart)
                } else {
                    btnFavoritePrd.setImageResource(R.drawable.hert)
                }

            }

            Glide.with(this)
                .load(it.img)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(binding.imgProductInf)

            productViewModel.getRatings(it.product_id)

            productViewModel.ratings.observe(this) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { ratings ->
                            if (ratings.isNotEmpty()) {
                                binding.tvViewAllReview.visibility = View.VISIBLE
                                ratingAdapter = RatingAdapter(ratings)
                                binding.rcvReviews.apply {
                                    layoutManager =
                                        LinearLayoutManager(this@ProductInformationActivity)
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
            }

            handleValueQuantityWithPrice(it.price)
        }
    }


    private fun setUpButtonToBasket() {
        val userEmail = SharedPreferencesHelper.getUserEmail(this)
        if (userEmail.isNullOrEmpty()) {
            DialogCustom.showLoginDialog(this)
        } else {
            startActivity(Intent(this, BasketActivity::class.java))
        }
    }

    private fun setUpButtonUpdateBasket() {
        val userEmail = SharedPreferencesHelper.getUserEmail(this)
        if (userEmail.isNullOrEmpty()) {
            DialogCustom.showLoginDialog(this)
        } else {
            val product: Product? = intent.getParcelableExtra("product")
            product?.let { product ->
                val productId = product.product_id
                val quantity = binding.tvQuantity.text.toString().toInt()
                basketViewModel.updateToBasket(quantity, productId, userEmail)
            }
            startActivity(Intent(this, BasketActivity::class.java))
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
        binding.btnUpdateBasket.text =
            "Update Basket - " + FormatterHelper.formatCurrency(totalPrice)
    }


}
