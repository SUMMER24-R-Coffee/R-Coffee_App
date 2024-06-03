package datlowashere.project.rcoffee.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.FragmentOrderHistoryBinding
import datlowashere.project.rcoffee.ui.viewmodel.HistoryViewModel


class OrderHistoryFragment : Fragment() {


    private var _binding : FragmentOrderHistoryBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val historyViewModel = ViewModelProvider(this).get(HistoryViewModel ::class.java)

        _binding = FragmentOrderHistoryBinding.inflate(inflater,container, false)
        val root: View = binding.root
        return root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }


}