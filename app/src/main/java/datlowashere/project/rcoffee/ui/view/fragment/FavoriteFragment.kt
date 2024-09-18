package datlowashere.project.rcoffee.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Favorite
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.repository.FavoriteRepository
import datlowashere.project.rcoffee.data.repository.HomeRepository
import datlowashere.project.rcoffee.databinding.FragmentFavoriteBinding
import datlowashere.project.rcoffee.ui.adapter.FavoriteProductAdapter
import datlowashere.project.rcoffee.ui.component.DialogCustom
import datlowashere.project.rcoffee.ui.view.activity.product.ProductInformationActivity
import datlowashere.project.rcoffee.ui.viewmodel.FavoriteViewModel
import datlowashere.project.rcoffee.ui.viewmodel.FavoriteViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.HomeViewModel
import datlowashere.project.rcoffee.ui.viewmodel.HomeViewModelFactory
import datlowashere.project.rcoffee.utils.Resource
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class FavoriteFragment : Fragment(), FavoriteProductAdapter.OnItemClickListener {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteProductAdapter: FavoriteProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeRepository = HomeRepository()
        val homeViewModelFactory = HomeViewModelFactory(homeRepository, requireContext())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        val favoriteRepository = FavoriteRepository()
        val favoriteViewModelFactory = FavoriteViewModelFactory(favoriteRepository)
        favoriteViewModel =
            ViewModelProvider(this, favoriteViewModelFactory)[FavoriteViewModel::class.java]

        //TODO: Update the case of last item->clicked and it already delete but have to click a few time to disappear
        favoriteProductAdapter = FavoriteProductAdapter(emptyList(), this)
        binding.rcvFavorite.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = favoriteProductAdapter
        }

        homeViewModel.getProductsWithFavorites(getEmail())

        homeViewModel.filteredProducts.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { products ->
                        Log.d("FavoriteFragment", "Filtered products fetched: ${products.size}")
                        favoriteProductAdapter.updateProducts(products)
                        binding.tvMessageFav.visibility =
                            if (products.isEmpty()) View.VISIBLE else View.GONE
                    }
                }

                is Resource.Error -> {
                    Log.e("FavoriteFragment", "Error fetching products: ${resource.message}")
                }

                is Resource.Loading -> {
                    Log.d("FavoriteFragment", "Loading products")
                }
            }
        })
    }

    override fun onFavoriteClick(product: Product) {
        if (getEmail().isNullOrBlank()) {
            DialogCustom.showLoginDialog(requireContext())
        } else {
            val userEmail = getEmail()
            val favorite = Favorite(0, product.product_id, userEmail)
            favoriteViewModel.insertOrDeleteFavorite(favorite)
            homeViewModel.getProductsWithFavorites(getEmail())
        }
    }

    override fun onFavoriteProductClick(product: Product) {
        val intent = Intent(requireContext(), ProductInformationActivity::class.java).apply {
            putExtra("product", product)
        }
        Log.d("PRODUCT_INFO", product.toString())
        startActivity(intent)
    }

    fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(requireContext()) ?: " "
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
