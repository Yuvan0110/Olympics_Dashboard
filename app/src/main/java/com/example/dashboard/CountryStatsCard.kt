package com.example.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dashboard.data.CountryStats
import com.example.dashboard.viewmodel.DashboardViewModel

@Composable
fun CountryStatsList(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel()
) {
    val stats by viewModel.countryStats.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCountryStats()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(stats) { stat ->
            CountryStatsCard(stat)
        }
    }
}

@Composable
fun CountryStatsCard(stats: CountryStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stats.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val ratio = if (stats.eventsParticipated == 0) 0.0
            else stats.totalMedals.toDouble() / stats.eventsParticipated

            Text("Events Participated: ${stats.eventsParticipated}")
            Text("Total Medals: ${stats.totalMedals}")
            Text("Medals/Event Ratio: ${"%.2f".format(ratio)}")
        }
    }
}

