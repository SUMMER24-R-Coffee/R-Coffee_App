package datlowashere.project.rcoffee.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.FragmentHomeFragmentBinding
import datlowashere.project.rcoffee.ui.viewmodel.HomeViewModel

class HomeFagment : Fragment() {

    private var _binding: FragmentHomeFragmentBinding? =null


    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeFragmentBinding.inflate(inflater, container, false)

        val textView: TextView = binding.tvHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val root: View = binding.root
        return root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }



}