package datlowashere.project.rcoffee.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import datlowashere.project.rcoffee.data.repository.HomeRepository
import datlowashere.project.rcoffee.databinding.FragmentHomeFragmentBinding
import datlowashere.project.rcoffee.ui.adapter.CategoryAdapter
import datlowashere.project.rcoffee.ui.viewmodel.HomeViewModel
import datlowashere.project.rcoffee.ui.viewmodel.HomeViewModelFactory
import datlowashere.project.rcoffee.utils.Resource
import java.util.*
import kotlin.collections.*
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoryAdapter: CategoryAdapter

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

        getBanner()
        getCategory()

    }

    fun getBanner(){
        homeViewModel.banners.observe(viewLifecycleOwner, Observer { bannerList ->
            bannerList?.let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val imageList = resource.data?.map { SlideModel(it.img, "") }
                        if (imageList != null) {
                            binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
                        }
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            }
        })
    }

    fun getCategory(){
        binding.rcvCategory.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        homeViewModel.categories.observe(viewLifecycleOwner, Observer { categoryList ->
            categoryList?.let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let {
                            categoryAdapter = CategoryAdapter(it)
                            binding.rcvCategory.adapter = categoryAdapter
                        }
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getBanner()
        homeViewModel.getCategories()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
