package datlowashere.project.rcoffee.ui.view.activity.basket

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.databinding.ActivityBastketBinding
import datlowashere.project.rcoffee.ui.adapter.BasketAdapter
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModelFactory
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class BastketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBastketBinding
    private lateinit var basketViewModel: BasketViewModel
    private lateinit var adapter: BasketAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBastketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
        setUpRecyclerView()
        setUpObservers()

        binding.btnBackBasket.setOnClickListener {
            finish()
        }
    }

    private fun setUpViewModel() {
        val repository = BasketRepository()
        val factory = BasketViewModelFactory(repository)
        basketViewModel = ViewModelProvider(this, factory).get(BasketViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        adapter = BasketAdapter(
            listOf(),
            onItemClicked = { basket -> handleItemClick(basket) },
            onCheckBoxClicked = { basket -> handleCheckBoxClick(basket) },
            onQuantityChanged = { basket, newQuantity -> handleQuantityChange(basket, newQuantity) },
            onRemoveClicked = { basket -> handleRemoveClick(basket) },
            this
        )
        binding.rcvBasket.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        binding.rcvBasket.adapter = adapter

    }
    private fun setUpObservers() {
        basketViewModel.baskets.observe(this, { baskets ->
            adapter.updateBaskets(baskets)
            if (baskets.isNullOrEmpty()) {
                binding.tvMessageBasket.visibility = View.VISIBLE
            } else {
                binding.tvMessageBasket.visibility = View.GONE
            }
        })
        basketViewModel.getBaskets(getEmail())

        basketViewModel.toastMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                basketViewModel.clearToastMessage()

            }

        })
    }

    private fun handleItemClick(basket: Basket) {
    }

    private fun handleCheckBoxClick(basket: Basket) {
    }

    private fun handleQuantityChange(basket: Basket, newQuantity: Int) {
        basket.quantity = newQuantity
        basketViewModel.updateQuantity(basket.basket_id, newQuantity)

    }

    private fun handleRemoveClick(basket: Basket) {
        basketViewModel.removeBasket(basket.basket_id) { success ->
            if (success) {
                basketViewModel.getBaskets(getEmail())
            } else {
                Toast.makeText(this, "Failed to remove basket item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getEmail(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: " "
    }
}
