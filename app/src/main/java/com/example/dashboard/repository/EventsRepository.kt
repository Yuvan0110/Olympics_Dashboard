package com.example.dashboard.repository

import android.util.Log
import com.example.dashboard.data.CountriesResponse
import com.example.dashboard.data.EventsResponse
import com.example.dashboard.network.RetrofitInstance

class EventsRepository {

    suspend fun fetchEvents(page: Int): EventsResponse? {
        return try {
            val response = RetrofitInstance.api.getEvents(page)
            if (response.isSuccessful) {
                val body = response.body()
                if (!body?.data.isNullOrEmpty()) {
                    body
                } else {
                    Log.d("EventsRepository", "Empty or null data in response body")
                    null
                }
            } else {
                Log.d("EventsRepository", "Response not successful: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("EventsRepository", "Exception while fetching data: ${e.localizedMessage}")
            null
        }
    }


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
}
