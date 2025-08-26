package com.example.dashboard.data

data class CountryStats(
    val countryId: String,
    val name: String,
    val totalMedals: Int,
    val eventsParticipated: Int,
    val medalRatio: Float
)
