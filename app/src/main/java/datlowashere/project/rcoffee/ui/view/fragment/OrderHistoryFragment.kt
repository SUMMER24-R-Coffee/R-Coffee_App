
package datlowashere.project.rcoffee.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import datlowashere.project.rcoffee.databinding.FragmentOrderHistoryBinding
import datlowashere.project.rcoffee.ui.adapter.OrderViewPage2Adapterr
import datlowashere.project.rcoffee.ui.viewmodel.HistoryViewModel

class OrderHistoryFragment : Fragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var orderViewPage2Adapterr: OrderViewPage2Adapterr

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        val viewPager = binding.viewpager2Ord
        val tabLayout = binding.tabLayoutOrd

        orderViewPage2Adapterr = OrderViewPage2Adapterr(this.childFragmentManager, lifecycle)

        viewPager.adapter = orderViewPage2Adapterr

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Pending"
                1 -> "Completed"
                2 -> "Cancelled"
                else -> "Pending"
            }
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}