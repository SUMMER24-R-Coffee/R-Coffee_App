package datlowashere.project.rcoffee.ui.view.activity.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.databinding.ActivityProductBinding
import datlowashere.project.rcoffee.data.repository.ProductRepository
import datlowashere.project.rcoffee.ui.adapter.ProductAdapter
import datlowashere.project.rcoffee.ui.component.DialogCustom
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModel
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModelFactory
import datlowashere.project.rcoffee.utils.GridSpacingItemDecoration
import datlowashere.project.rcoffee.utils.Resource
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class ProductActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

    private lateinit var binding: ActivityProductBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var basketViewModel: BasketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()


        productViewModel.getProducts(getEmail())

        observeViewModel()

        binding.btnBackProduct.setOnClickListener {
            finish()
        }
    }

    private fun setUpViewModel(){
        val repository = ProductRepository()
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)

        val basketRepository = BasketRepository()
        val basketViewModelFactory = BasketViewModelFactory(basketRepository)
        basketViewModel = ViewModelProvider(this, basketViewModelFactory).get(BasketViewModel::class.java)
    }
    private fun observeViewModel() {
        productViewModel.products.observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        productAdapter = ProductAdapter(it, this)
                        binding.rcvProducts.layoutManager = GridLayoutManager(this, 2)
                        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
                        binding.rcvProducts.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true))
                        binding.rcvProducts.adapter = productAdapter
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })

        basketViewModel.toastMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                basketViewModel.clearToastMessage()
            }
        })

    }
    fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: " "
    }
    override fun onProductClick(product: Product) {
        val intent = Intent(this, ProductInformationActivity::class.java).apply {
            putExtra("product", product)
        }
        Log.d("PRODUCT_INFO", product.toString())
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
}
