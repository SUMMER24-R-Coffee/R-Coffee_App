package datlowashere.project.rcoffee.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import datlowashere.project.rcoffee.data.repository.HomeRepository
import datlowashere.project.rcoffee.databinding.FragmentHomeFragmentBinding
import datlowashere.project.rcoffee.ui.adapter.CategoryAdapter
import datlowashere.project.rcoffee.ui.adapter.ProductAdapter
import datlowashere.project.rcoffee.ui.viewmodel.HomeViewModel
import datlowashere.project.rcoffee.ui.viewmodel.HomeViewModelFactory
import datlowashere.project.rcoffee.utils.Resource

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = HomeRepository()
        val viewModelFactory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        setupObservers()

        homeViewModel.getData()
    }

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
                        categoryAdapter = CategoryAdapter(it, object : CategoryAdapter.OnItemClickListener {
                            override fun onItemClick(categoryId: Int) {
                                homeViewModel.filterProductsByCategory(categoryId)
                            }
                        })
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


        homeViewModel.products.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        productAdapter = ProductAdapter(it)
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

        homeViewModel.products.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        productAdapter = ProductAdapter(it)
                        binding.rcvRecommend.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        binding.rcvRecommend.adapter = productAdapter
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
                        productAdapter = ProductAdapter(it)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
