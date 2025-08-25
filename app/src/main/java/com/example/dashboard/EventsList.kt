package com.example.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dashboard.data.Competitors
import com.example.dashboard.viewmodel.DashboardViewModel


@Composable
fun DisplayEvents(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        dashboardViewModel.loadEvents(1)
    }

    val events by dashboardViewModel.events.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(events) { event ->
            EventItem(
                name = event.eventName,
                competitors = event.competitors
            )
        }
    }
}

@Composable
fun EventItem(
    name: String?,
    competitors: List<Competitors>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = name ?: "Unnamed Event"
            )

            Spacer(modifier = Modifier.height(8.dp))

            competitors.forEach {
                Text(text = "- ${it.competitorName ?: "Unknown"} (${it.countryId ?: "N/A"})")
            }
        }
    }
}


