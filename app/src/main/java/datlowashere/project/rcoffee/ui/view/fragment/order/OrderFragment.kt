package datlowashere.project.rcoffee.ui.view.fragment.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.data.repository.OrderRepository
import datlowashere.project.rcoffee.databinding.FragmentOrderBinding
import datlowashere.project.rcoffee.ui.adapter.OrderItemAdapter
import datlowashere.project.rcoffee.ui.view.activity.order.OrderInnformationActivity
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper
import datlowashere.project.rcoffee.viewmodel.OrderViewModel
import datlowashere.project.rcoffee.viewmodel.OrderViewModelFactory

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderItemAdapter: OrderItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderRepository = OrderRepository()
        val viewModelFactory = OrderViewModelFactory(orderRepository)
        orderViewModel = ViewModelProvider(this, viewModelFactory).get(OrderViewModel::class.java)

        orderItemAdapter = OrderItemAdapter(
            listOf(),
            onItemClicked = { order ->
                handleItemClick(order)
            },

            )

        binding.rcvOrder.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = orderItemAdapter
        }

        orderViewModel.orders.observe(viewLifecycleOwner) { orders ->
            val filteredOrders = orders.filter {
                it.status_order != "cancelled" && it.status_order != "delivered"
            }
            binding.tvMessageOrderFragment.visibility =
                if (filteredOrders.isEmpty()) View.VISIBLE else View.GONE

            orderItemAdapter.updateOrders(filteredOrders)
        }
        orderViewModel.getOrders(getEmail())
    }

    private fun handleItemClick(order: Order) {
        val intent = Intent(requireContext(), OrderInnformationActivity::class.java)
        intent.putExtra("ORDER", order)
        startActivity(intent)
    }

    private fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(requireContext()) ?: " "

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
