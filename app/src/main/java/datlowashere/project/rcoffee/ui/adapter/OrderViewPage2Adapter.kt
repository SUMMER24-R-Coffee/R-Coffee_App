package datlowashere.project.rcoffee.ui.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import datlowashere.project.rcoffee.ui.view.fragment.order.OrderCancelFragment
import datlowashere.project.rcoffee.ui.view.fragment.order.OrderCompletedFragment
import datlowashere.project.rcoffee.ui.view.fragment.order.OrderFragment

class OrderViewPage2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderFragment()
            1 -> OrderCompletedFragment()
            2 -> OrderCancelFragment()
            else -> OrderFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}