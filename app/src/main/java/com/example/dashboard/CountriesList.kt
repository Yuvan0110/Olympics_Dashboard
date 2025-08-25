package com.example.dashboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dashboard.viewmodel.DashboardViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun GroupedCountryMedalsBarChart(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val countries by dashboardViewModel.countries.collectAsState()

    LaunchedEffect(Unit) {
        dashboardViewModel.loadCountries()
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp),
        factory = { context -> HorizontalBarChart(context) },
        update = { chart ->

            if (countries.isNotEmpty()) {
                val goldEntries = ArrayList<BarEntry>()
                val silverEntries = ArrayList<BarEntry>()
                val bronzeEntries = ArrayList<BarEntry>()

                countries.forEachIndexed { index, country ->
                    goldEntries.add(BarEntry(index.toFloat(), country.goldMedals?.toFloat() ?: 0f))
                    silverEntries.add(BarEntry(index.toFloat(), country.silverMedals?.toFloat() ?: 0f))
                    bronzeEntries.add(BarEntry(index.toFloat(), country.bronzeMedals?.toFloat() ?: 0f))
                }

                val goldSet = BarDataSet(goldEntries, "Gold").apply { color = android.graphics.Color.rgb(255, 215, 0) }
                val silverSet = BarDataSet(silverEntries, "Silver").apply { color = android.graphics.Color.GRAY }
                val bronzeSet = BarDataSet(bronzeEntries, "Bronze").apply { color = android.graphics.Color.rgb(205, 127, 50) }

                val barData = BarData(goldSet, silverSet, bronzeSet)
                val groupSpace = 0.2f
                val barSpace = 0.05f
                val barWidth = 0.25f

                barData.barWidth = barWidth
                chart.data = barData

                val labels = countries.map { it.id ?: "" }

                chart.xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    labelRotationAngle = -45f
                    labelCount = countries.size
                    axisMinimum = 0f
                }

                chart.axisLeft.axisMinimum = 0f
                chart.axisRight.isEnabled = false
                chart.description.isEnabled = false
                chart.legend.isEnabled = true


                chart.xAxis.axisMaximum = countries.size.toFloat()
                chart.groupBars(0f, groupSpace, barSpace)

                chart.isDragEnabled = true
                chart.setScaleEnabled(true)
                chart.setPinchZoom(true)
                chart.setVisibleXRangeMaximum(8f)
                chart.moveViewToX(0f)
                chart.invalidate()
            }
        }
    )
}


