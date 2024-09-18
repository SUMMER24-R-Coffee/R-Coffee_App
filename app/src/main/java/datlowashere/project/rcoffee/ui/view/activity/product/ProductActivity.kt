package datlowashere.project.rcoffee.ui.view.activity.product

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Favorite
import datlowashere.project.rcoffee.data.model.PriceRange
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.data.repository.FavoriteRepository
import datlowashere.project.rcoffee.data.repository.ProductRepository
import datlowashere.project.rcoffee.databinding.ActivityProductBinding
import datlowashere.project.rcoffee.ui.adapter.ProductAdapter
import datlowashere.project.rcoffee.ui.component.DialogCustom
import datlowashere.project.rcoffee.ui.component.FilterBottomSheetFragment
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.FavoriteViewModel
import datlowashere.project.rcoffee.ui.viewmodel.FavoriteViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModel
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModelFactory
import datlowashere.project.rcoffee.utils.GridSpacingItemDecoration
import datlowashere.project.rcoffee.utils.Resource
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class ProductActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener,
    FilterBottomSheetFragment.FilterListener {

    private lateinit var binding: ActivityProductBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var basketViewModel: BasketViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var allProducts: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()

        binding.btnBackProduct.setOnClickListener {
            finish()
        }

        binding.edSearchProduct.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProductsByName(s.toString())
            }
        })

        binding.btnFiletrProduct.setOnClickListener {
            FilterBottomSheetFragment().show(supportFragmentManager, "FilterBottomSheetFragment")
        }

        setupRecyclerView()
        observeViewModel()
        productViewModel.getProducts(getEmail())
    }

    private fun setUpViewModel() {
        val repository = ProductRepository()
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)

        val basketRepository = BasketRepository()
        val basketViewModelFactory = BasketViewModelFactory(basketRepository)
        basketViewModel =
            ViewModelProvider(this, basketViewModelFactory).get(BasketViewModel::class.java)

        val favoriteRepository = FavoriteRepository()
        val favoriteViewModelFactory = FavoriteViewModelFactory(favoriteRepository)
        favoriteViewModel =
            ViewModelProvider(this, favoriteViewModelFactory).get(FavoriteViewModel::class.java)
    }

    private fun observeViewModel() {
        productViewModel.products.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        allProducts = it
                        filterProductsByName(binding.edSearchProduct.text.toString())
                        updateMessageVisibility()
                    }
                }

                is Resource.Error -> {
                }

                is Resource.Loading -> {
                }
            }
        }

        basketViewModel.toastMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                basketViewModel.clearToastMessage()
            }
        })
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList(), this)
        binding.rcvProducts.apply {
            layoutManager = GridLayoutManager(this@ProductActivity, 2)
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
            addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true))
            adapter = productAdapter
        }
    }

    override fun onProductClick(product: Product) {
        val intent = Intent(this, ProductInformationActivity::class.java).apply {
            putExtra("product", product)
        }
        startActivity(intent)
    }

    override fun onAddBasketClick(product: Product) {
        if (getEmail().isNullOrBlank()) {
            DialogCustom.showLoginDialog(this)
        } else {
            val userEmail = getEmail()
            basketViewModel.addOrUpdateBasket(product.product_id, userEmail)
        }
    }

    override fun onFavoriteClick(product: Product) {
        if (getEmail().isNullOrBlank()) {
            DialogCustom.showLoginDialog(this)
        } else {
            val userEmail = getEmail()
            val favorite = Favorite(0, product.product_id, userEmail)
            favoriteViewModel.insertOrDeleteFavorite(favorite)
        }
    }

    private fun filterProductsByName(query: String) {
        val filteredProducts = if (query.isEmpty()) {
            allProducts
        } else {
            allProducts.filter {
                it.product_name.contains(query, ignoreCase = true)
            }
        }
        productAdapter.updateProducts(filteredProducts)
        updateMessageVisibility()
    }

    private fun filterProductsByPrice(priceRange: PriceRange) {
        val filteredProducts = when (priceRange) {
            PriceRange.ALL -> allProducts
            PriceRange.LESS_THAN_20000 -> allProducts.filter { it.price < 20000 }
            PriceRange.BETWEEN_20000_AND_40000 -> allProducts.filter { it.price in 20000.0..40000.0 }
            PriceRange.GREATER_THAN_40000 -> allProducts.filter { it.price > 40000 }
        }
        productAdapter.updateProducts(filteredProducts)
        updateMessageVisibility()
    }

    private fun updateMessageVisibility() {
        if (productAdapter.itemCount == 0) {
            binding.tvMessageProduct.visibility = View.VISIBLE
        } else {
            binding.tvMessageProduct.visibility = View.GONE
        }
    }

    private fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: " "
    }

    override fun onFilterSelected(priceRange: PriceRange) {
        filterProductsByPrice(priceRange)
    }
}
