package datlowashere.project.rcoffee.ui.view.activity.basket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import datlowashere.project.rcoffee.data.model.Basket
import datlowashere.project.rcoffee.data.repository.BasketRepository
import datlowashere.project.rcoffee.databinding.ActivityBastketBinding
import datlowashere.project.rcoffee.ui.adapter.BasketAdapter
import datlowashere.project.rcoffee.ui.view.activity.order.OrderActivity
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModel
import datlowashere.project.rcoffee.ui.viewmodel.BasketViewModelFactory
import datlowashere.project.rcoffee.utils.FormatterHelper
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class BasketActivity : AppCompatActivity() {

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
        binding.btnCheckoutBasket.setOnClickListener {
            val selectedBaskets = adapter.getSelectedBaskets()
            val totalAmount = adapter.getTotalAmount()

            if (totalAmount == 0.0) {
                Toast.makeText(this, "Please, select item", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, OrderActivity::class.java)
                intent.putParcelableArrayListExtra("selectedBaskets", ArrayList(selectedBaskets))
                intent.putExtra("totalAmount", totalAmount)
                startActivity(intent)
            }

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
            onQuantityChanged = { basket, newQuantity ->
                handleQuantityChange(
                    basket,
                    newQuantity
                )
            },
            onRemoveClicked = { basket -> handleRemoveClick(basket) },
            onTotalAmountChanged = { totalAmount -> updateTotalAmount(totalAmount) },
            this
        )
        binding.rcvBasket.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvBasket.adapter = adapter

    }

    private fun setUpObservers() {
        basketViewModel.baskets.observe(this) { baskets ->
            adapter.updateBaskets(baskets)
            binding.tvMessageBasket.visibility = if (baskets.isEmpty()) View.VISIBLE else View.GONE

        }
        basketViewModel.getBaskets(getEmail())

        basketViewModel.toastMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                basketViewModel.clearToastMessage()

            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalAmount(totalAmount: Double) {
        binding.tvTotalAmountBasket.text =
            "Total Amount:\n" + FormatterHelper.formatCurrency(totalAmount)
        binding.btnCheckoutBasket.isEnabled = totalAmount != 0.0

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
