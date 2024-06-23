package datlowashere.project.rcoffee.ui.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Favorite
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.data.repository.FavoriteRepository
import datlowashere.project.rcoffee.data.repository.HomeRepository
import datlowashere.project.rcoffee.databinding.FragmentHomeFragmentBinding
import datlowashere.project.rcoffee.ui.adapter.CategoryAdapter
import datlowashere.project.rcoffee.ui.adapter.ProductAdapter
import datlowashere.project.rcoffee.ui.component.DialogCustom
import datlowashere.project.rcoffee.ui.view.activity.basket.BastketActivity
import datlowashere.project.rcoffee.ui.view.activity.product.ProductActivity
import datlowashere.project.rcoffee.ui.view.activity.product.ProductInformationActivity
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModel
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.FavoriteViewModel
import datlowashere.project.rcoffee.ui.viewmodel.FavoriteViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.HomeViewModel
import datlowashere.project.rcoffee.ui.viewmodel.HomeViewModelFactory
import datlowashere.project.rcoffee.utils.Resource
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class HomeFragment : Fragment(), CategoryAdapter.OnItemClickListener, ProductAdapter.OnItemClickListener {

    private var _binding: FragmentHomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var authViewModel: AuthViewModel
    private lateinit var basketViewModel: BasketViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModels()
        setupObservers()

        homeViewModel.getData()
        authViewModel.getUserData(getEmail())
        homeViewModel.getProducts(getEmail())

        Log.d("EMAIL USER", getEmail())

        binding.tvTitleCateory.setOnClickListener {
            startActivity(Intent(context, ProductActivity::class.java))
        }
        binding.fabBasket.setOnClickListener {
            setUpButtonBasket()
        }
    }

    private fun setupViewModels() {
        val homeRepository = HomeRepository()
        val homeViewModelFactory = HomeViewModelFactory(homeRepository, requireContext())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        val authRepository = AuthRepository()
        val authViewModelFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)

        val basketRepository = BasketRepository()
        val basketViewModelFactory = BasketViewModelFactory(basketRepository)
        basketViewModel = ViewModelProvider(this, basketViewModelFactory).get(BasketViewModel::class.java)

        val favoriteRepository = FavoriteRepository()
        val favoriteViewModelFactory = FavoriteViewModelFactory(favoriteRepository)
        favoriteViewModel= ViewModelProvider(this, favoriteViewModelFactory).get(FavoriteViewModel::class.java)
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun setupObservers() {
        homeViewModel.banners.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    val imageList = resource.data?.map { SlideModel(it.img, "") }
                    imageList?.let { binding.imageSlider.setImageList(it, ScaleTypes.CENTER_CROP) }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })

        homeViewModel.categories.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        categoryAdapter = CategoryAdapter(it, this@HomeFragment)
                        binding.rcvCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        binding.rcvCategory.adapter = categoryAdapter
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })

        homeViewModel.products.observe(this@HomeFragment, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {products ->
                    if(products.isNotEmpty()){
                        productAdapter = ProductAdapter(products, this@HomeFragment)
                        binding.rcvProduct.apply {
                            layoutManager =LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter=productAdapter
                        }

                        binding.rcvRecommend.apply {
                            layoutManager =LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            adapter=productAdapter
                        }
                    }

                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })

        homeViewModel.filteredProducts.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        productAdapter = ProductAdapter(it, this@HomeFragment)
                        binding.rcvProduct.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        binding.rcvProduct.adapter = productAdapter
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
            }
        })

        authViewModel.userResponse.observe(viewLifecycleOwner, Observer { authResponse ->
            if (authResponse != null) {
                authResponse.users?.let { users ->
                    if (users.isNotEmpty()) {
                        val user = users[0]
                        binding.tvNameUser.text = "Hello, Welcome back\n${user.name}!"
                        Glide.with(binding.imgUser)
                            .load(user.user_img)
                            .centerCrop()
                            .placeholder(R.drawable.img_default)
                            .into(binding.imgUser)

                        user.name?.let { Log.d("User Info", "Name: $it") }
                        user.user_img?.let { Log.d("User Info", "Image URL: $it") }
                    }
                }
            } else {
                Log.e("HomeFragment", "User response is null")
            }
        })

        basketViewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                basketViewModel.clearToastMessage()

            }

        })
        //Toast message for clicked img favorite

//        favoriteViewModel.favoriteResponse.observe(this) { favorite ->
//            favorite?.let {
//                Toast.makeText(requireContext(), "Favorite operation successful: ${it}", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//
//        favoriteViewModel.errorMessage.observe(this) { message ->
//            message?.let {
//                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
//            }
//        }

    }

    override fun onItemClick(categoryId: Int) {
        homeViewModel.filterProductsByCategory(categoryId)
    }

    fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(requireContext()) ?: " "
    }

    fun setUpButtonBasket() {
        val userEmail = SharedPreferencesHelper.getUserEmail(requireContext())
        if (userEmail.isNullOrEmpty()) {
            DialogCustom.showLoginDialog(requireContext())
        } else {
            startActivity(Intent(requireContext(), BastketActivity::class.java))

        }
    }

    override fun onProductClick(product: Product) {
        val intent = Intent(context, ProductInformationActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }

    override fun onAddBasketClick(product: Product) {
        if (getEmail().isNullOrBlank()) {
            DialogCustom.showLoginDialog(requireContext())
        } else {
            val userEmail = getEmail()
            basketViewModel.addOrUpdateBasket(product.product_id, userEmail)
        }
    }

    override fun onFavoriteClick(product: Product) {
        if (getEmail().isNullOrBlank()) {
            DialogCustom.showLoginDialog(requireContext())
        } else {
            val userEmail = getEmail()
            val favorite = Favorite(0,product.product_id,userEmail)

            favoriteViewModel.insertOrDeleteFavorite(favorite)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
