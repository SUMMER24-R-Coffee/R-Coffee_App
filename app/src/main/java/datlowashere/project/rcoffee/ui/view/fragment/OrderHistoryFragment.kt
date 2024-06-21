
package datlowashere.project.rcoffee.ui.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.FragmentOrderHistoryBinding
import datlowashere.project.rcoffee.ui.adapter.OrderViewPage2Adapterr
import datlowashere.project.rcoffee.ui.viewmodel.HistoryViewModel

class OrderHistoryFragment : Fragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var orderViewPage2Adapterr: OrderViewPage2Adapterr
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        viewPager = binding.viewpager2Ord
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
            tab.customView = createTabView(tab)


        }.attach()

        checkAndSwitchToCancelledTab()

        return binding.root
    }
    private fun createTabView(tab: TabLayout.Tab): View {
        val tabTextView = TextView(requireContext())
        tabTextView.text = tab.text
        tabTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tabTextView.textSize = 16f
        tabTextView.isAllCaps = false
        tabTextView.typeface = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        tabTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        return tabTextView
    }
    private fun checkAndSwitchToCancelledTab() {
        val switchToCancelled = arguments?.getBoolean("SWITCH_TO_CANCELLED", false) ?: false
        if (switchToCancelled) {
            viewPager.post {
                switchToCancelledOrdersTab()
            }
        }
    }

    fun switchToCancelledOrdersTab() {
        Log.d("OrderHistoryFragment", "Switching to Cancelled Orders Tab")
        binding.viewpager2Ord.currentItem = 2
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}