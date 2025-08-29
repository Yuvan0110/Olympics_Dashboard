package com.example.dashboard

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dashboard.viewmodel.DashboardViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

@Composable
fun GroupedByGender(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        dashboardViewModel.eventsGroupedByMenAndWomen()
    }

    val menCount by dashboardViewModel.menEvents.collectAsState()
    val womenCount by dashboardViewModel.womenEvents.collectAsState()
    val isDataLoaded by dashboardViewModel.isDataLoaded.collectAsState()

    if (!isDataLoaded) {
        Box(
            modifier = modifier
                .height(300.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading data...")
        }
    } else {
        Box(
            modifier = modifier
        ) {
            Text("Sports categorized by gender")
            Spacer(Modifier.height(10.dp))
            AndroidView(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
                factory = { context -> PieChart(context) },
                update = { chart ->
                    val entries = listOf(
                        PieEntry(menCount.toFloat(), "Men"),
                        PieEntry(womenCount.toFloat(), "Women")
                    )

                    val dataset = PieDataSet(entries, "").apply {
                        colors = listOf(
                            Color.parseColor("#8BC34A"),
                            Color.parseColor("#36A2EB")
                        )
                        valueTextSize = 12f
                    }

                    val pieData = PieData(dataset).apply {
                        setValueFormatter(PercentFormatter(chart))
                    }

                    chart.isDrawHoleEnabled = false
                    chart.data = pieData
                    chart.setUsePercentValues(true)
                    chart.description.isEnabled = false
                    chart.setEntryLabelColor(Color.BLACK)
                    chart.setEntryLabelTextSize(12f)

                    chart.legend.apply {
                        isEnabled = true
                        textSize = 12f
                        verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    }

                    chart.invalidate()
                }
            )
        }
    }
}
