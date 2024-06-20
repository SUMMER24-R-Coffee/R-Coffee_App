package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.network.ApiClient
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

    suspend fun updateStatusOrder(orderId: String,statusOrder: String, callback: (Boolean) -> Unit) {
        apiService.updateStatusOrder( orderId,statusOrder).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }
}
