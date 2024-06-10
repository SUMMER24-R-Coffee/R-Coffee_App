package datlowashere.project.rcoffee.ui.view.activity.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.databinding.ActivityProductBinding
import datlowashere.project.rcoffee.data.repository.ProductRepository
import datlowashere.project.rcoffee.ui.adapter.ProductAdapter
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModel
import datlowashere.project.rcoffee.ui.viewmodel.ProductViewModelFactory
import datlowashere.project.rcoffee.utils.GridSpacingItemDecoration
import datlowashere.project.rcoffee.utils.Resource

class ProductActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

    private lateinit var binding: ActivityProductBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = ProductRepository()
        val factory = ProductViewModelFactory(repository)
        productViewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)

        observeViewModel()

        binding.btnBackProduct.setOnClickListener {
            finish()
        }
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
    }

    override fun onProductClick(product: Product) {
        val intent = Intent(this, ProductInformationActivity::class.java).apply {
            putExtra("product", product)
        }
        Log.d("PRODUCT_INFO", product.toString())
        startActivity(intent)
    }
}
