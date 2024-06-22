package datlowashere.project.rcoffee.data.repository

import android.util.Log
import datlowashere.project.rcoffee.data.model.PaymentDetail
import datlowashere.project.rcoffee.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentRepository {
    private val apiService = ApiClient.instance

    suspend fun insertPaymentDetail(paymentDetail: PaymentDetail, callback: (Boolean) -> Unit) {
        apiService.insertPaymentDetail(paymentDetail).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback(false)
            }
        })
    }

    suspend fun updatePaymentStatus(orderId: String, paymentDetail: PaymentDetail, callback: (Boolean) -> Unit) {
        apiService.updatePaymentDetail(orderId, paymentDetail).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("PaymentRepository", "Response received: Code=${response.code()}, Message=${response.message()}")
                if(response.isSuccessful ||response.code() ==203){
                    Log.d("PaymentRepository", "success")
                    callback(true)

                }else{
                    Log.e("PaymentRepository", "Order status update failed: ${response.errorBody()?.string()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Payment Repository", "Failed"+t.message)
                callback(false)
            }
        })
    }
}
