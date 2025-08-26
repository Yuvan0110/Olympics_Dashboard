package com.example.dashboard.repository


import android.util.Log
import com.example.dashboard.data.CountriesResponse
import com.example.dashboard.data.EventsResponse
import com.example.dashboard.network.RetrofitInstance


class DashboardsRepository {
    suspend fun fetchCountries(): CountriesResponse? {
        return try {
            val response = RetrofitInstance.api.getCountries()
            if (response.isSuccessful && response.body()?.data?.isNotEmpty()!!) {
                return response.body()
            } else {
                Log.d("Error", "Response not fetched correctly")
                null
            }
        }catch(e : Exception) {
            Log.d("Error", e.javaClass.name)
            null
        }
    }

    suspend fun fetchAllEvents(): EventsResponse? {
        val response = RetrofitInstance.api.getEvents(1)
        return if (response.isSuccessful && response.body()?.data?.isNotEmpty() == true) {
            response.body()
        } else {
            null
        }
    }
}