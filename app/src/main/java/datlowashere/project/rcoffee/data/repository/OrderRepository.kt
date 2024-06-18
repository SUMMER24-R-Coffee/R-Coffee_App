package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Order
import datlowashere.project.rcoffee.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository {
    private val apiService = ApiClient.instance

    suspend fun orderItem(order: Order, callback: (Order?) -> Unit) {
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
}
