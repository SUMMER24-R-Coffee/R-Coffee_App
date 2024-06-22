package datlowashere.project.rcoffee.data.repository

import android.util.Log
import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.data.model.response.OrderResponse
import datlowashere.project.rcoffee.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository {
    private val apiService = ApiClient.instance

    suspend fun insertOrder(order: Order, callback: (Order?) -> Unit) {
        apiService.orderItem(order).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                callback(null)
            }
        })
    }


    fun getOrders(emailUser: String, callback: (List<Order>?) -> Unit) {
        apiService.getOrders(emailUser).enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun updateStatusOrder(orderId: String, order: OrderResponse, callback: (Boolean) -> Unit) {
        apiService.updateStatusOrder(orderId, order).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("OrderRepository", "Response received: Code=${response.code()}, Message=${response.message()}")
                if (response.isSuccessful || response.code() == 204) {
                    Log.d("OrderRepository", "Order status update successful")
                    callback(true)
                } else {
                    Log.e("OrderRepository", "Order status update failed: ${response.errorBody()?.string()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("OrderRepository", "Order status update failed: ${t.message}")
                callback(false)
            }
        })
    }


}
