package com.example.dashboard

import android.content.Context
import android.widget.TextView
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF


@Composable
fun DisciplineCountries(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        dashboardViewModel.loadDisciplineCountries()
    }

    val disciplineCountryMap by dashboardViewModel.disciplineCountryMap.collectAsState()

    val disciplines = disciplineCountryMap.keys.toList()
    if(disciplineCountryMap.isNotEmpty()) {
        Box(
            modifier = modifier
        ) {
            Text("Countries participated by sports")
            Spacer(Modifier.height(10.dp))
            AndroidView(
                modifier = modifier
                    .fillMaxWidth()
                    .height(300.dp),
                factory = { context -> BarChart(context) },
                update = { chart ->
                    val entries = disciplines.mapIndexed { index, discipline ->
                        BarEntry(
                            index.toFloat(),
                            disciplineCountryMap[discipline]?.size?.toFloat() ?: 0f
                        )
                    }

                    val barDataset = BarDataSet(entries, "")

                    val barData = BarData(barDataset)

                    chart.data = barData

                    chart.xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(disciplines)
                        position = XAxis.XAxisPosition.BOTTOM
                        granularity = 1f
                        setDrawGridLines(false)
                        labelRotationAngle = -45f
                        labelCount = disciplines.size
                    }

                    chart.axisLeft.setDrawGridLines(false)
                    chart.axisLeft.axisMinimum = 0f
                    chart.axisRight.isEnabled = false
                    chart.description.isEnabled = false
                    chart.isDragEnabled = true
                    chart.setScaleEnabled(true)
                    chart.setPinchZoom(true)
                    chart.setVisibleXRangeMaximum(6f)
                    chart.moveViewToX(0f)

                    val markerView = CustomMarkerView(chart.context, R.layout.custom_marker_view, disciplines)
                    markerView.chartView = chart // Very important!
                    chart.marker = markerView

                    chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
//                            chart.animateX(500, Easing.EaseOutBack)
                        }

                        override fun onNothingSelected() {
                            chart.highlightValues(null)
                        }
                    })

                    chart.invalidate()
                }
            )
        }
    }else{
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading chart data...")
        }
    }
}


class CustomMarkerView(
    context: Context,
    layoutResource: Int,
    private val disciplines: List<String>
) : MarkerView(context, layoutResource) {

    private val tvContent: TextView = findViewById(R.id.tvContent)
    private val tvXLabel: TextView = findViewById(R.id.tvXLabel)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            tvContent.text = "Countries: ${it.y.toInt()}"
            tvXLabel.text = "Discipline: ${disciplines[it.x.toInt()]}"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}

