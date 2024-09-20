
package datlowashere.project.rcoffee.ui.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.FragmentOrderHistoryBinding
import datlowashere.project.rcoffee.ui.adapter.OrderViewPage2Adapter
import datlowashere.project.rcoffee.ui.viewmodel.HistoryViewModel

class OrderHistoryFragment : Fragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var orderViewPage2Adapter: OrderViewPage2Adapter
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        viewPager = binding.viewpager2Ord
        val tabLayout = binding.tabLayoutOrd

        orderViewPage2Adapter = OrderViewPage2Adapter(this.childFragmentManager, lifecycle)

        viewPager.adapter = orderViewPage2Adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Pending"
                1 -> "Completed"
                2 -> "Cancelled"
                else -> "Pending"
            }
            tab.customView = createTabView(tab)


        }.attach()

        checkAndSwitchTabs()

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
    private fun checkAndSwitchTabs() {
        val switchToCancelled = arguments?.getBoolean("SWITCH_TO_CANCELLED", false) ?: false
        val switchToCompleted = arguments?.getBoolean("SWITCH_TO_COMPLETED", false) ?: false
        val switchToPending = arguments?.getBoolean("SWITCH_TO_PENDING", false) ?: false
        when {
            switchToCancelled -> binding.viewpager2Ord.post { switchTab(2) }
            switchToCompleted -> binding.viewpager2Ord.post { switchTab(1) }
            switchToPending -> binding.viewpager2Ord.post { switchTab(0) }
        }
    }

    private fun switchTab(index: Int) {
        Log.d("OrderHistoryFragment", "Switching to Tab Index: $index")
        binding.viewpager2Ord.currentItem = index
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}