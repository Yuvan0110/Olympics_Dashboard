package com.example.dashboard

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun CountryMedalsBarChart(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val countries by dashboardViewModel.countries.collectAsState()

    LaunchedEffect(Unit) {
        dashboardViewModel.loadCountries()
    }
    Box(
        modifier = modifier
    ) {
        Text("Medals by country")
        Spacer(Modifier.height(10.dp))
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context -> BarChart(context) },
            update = { chart ->

                if (countries.isNotEmpty()) {
                    val goldEntries = ArrayList<BarEntry>()
                    val silverEntries = ArrayList<BarEntry>()
                    val bronzeEntries = ArrayList<BarEntry>()

                    countries.forEachIndexed { index, country ->
                        goldEntries.add(
                            BarEntry(
                                index.toFloat(),
                                country.goldMedals?.toFloat() ?: 0f
                            )
                        )
                        silverEntries.add(
                            BarEntry(
                                index.toFloat(),
                                country.silverMedals?.toFloat() ?: 0f
                            )
                        )
                        bronzeEntries.add(
                            BarEntry(
                                index.toFloat(),
                                country.bronzeMedals?.toFloat() ?: 0f
                            )
                        )
                    }

                    val goldSet =
                        BarDataSet(goldEntries, "Gold").apply { color = Color.rgb(255, 215, 0) }
                    val silverSet = BarDataSet(silverEntries, "Silver").apply { color = Color.GRAY }
                    val bronzeSet = BarDataSet(bronzeEntries, "Bronze").apply {
                        color = Color.rgb(205, 127, 50)
                    }

                    val barData = BarData(goldSet, silverSet, bronzeSet)

                    barData.barWidth = 0.25f
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
                    chart.legend.apply {
                        isEnabled = true
                        verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    }

                    chart.xAxis.axisMaximum = countries.size.toFloat()
                    chart.groupBars(0f, 0.2f, 0.05f)


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
}