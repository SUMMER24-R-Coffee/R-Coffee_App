package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Voucher
import datlowashere.project.rcoffee.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoucherRepository {
    private val apiService = ApiClient.instance

    suspend fun getVouchers(callback: (List<Voucher>?) -> Unit) {
        apiService.getVoucher().enqueue(object : Callback<List<Voucher>> {
            override fun onResponse(call: Call<List<Voucher>>, response: Response<List<Voucher>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Voucher>>, t: Throwable) {
                callback(null)
            }
        })
    }
}
