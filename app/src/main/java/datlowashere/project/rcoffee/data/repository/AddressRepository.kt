package datlowashere.project.rcoffee.data.repository

import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class AddressRepository {
    private val apiService = ApiClient.instance

    suspend fun getAddresses(emailUser: String, callback: (List<Address>?) -> Unit) {
        apiService.getAddresses(emailUser).enqueue(object : Callback<List<Address>> {
            override fun onResponse(call: Call<List<Address>>, response: Response<List<Address>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Address>>, t: Throwable) {
                callback(null)
            }
        })
    }

    suspend fun addAddress(address: Address, callback: (Address?) -> Unit) {
        apiService.addAddress(address).enqueue(object : Callback<Address> {
            override fun onResponse(call: Call<Address>, response: Response<Address>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Address>, t: Throwable) {
                callback(null)
            }
        })
    }
}
