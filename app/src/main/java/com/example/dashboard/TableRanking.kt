package com.example.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dashboard.viewmodel.DashboardViewModel


@Composable
fun TableRanking(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        dashboardViewModel.getTableRanking()
    }

    val tableRanking by dashboardViewModel.tableRanking.collectAsState()

    Box(modifier = modifier.fillMaxSize().padding(30.dp)) {
        Column {
                Row {
                    TableCell("Country Name", height = 50.dp)
                    TableCell("Total medals", height = 50.dp)
                    TableCell("Ranking", height = 50.dp)
                    TableCell("Ranked upon\nmedal count", height = 50.dp)
                }
            LazyColumn {
                items(tableRanking) { item ->
                    Row {
                        TableCell(item.countryId ?: "")
                        TableCell(item.countryTotalMedals.toString())
                        TableCell(item.countryRank.toString())
                        TableCell(item.countryRankedByMedals.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun TableCell(
    text: String,
    height: Dp = 25.dp
) {
    Box( modifier = Modifier
        .width(80.dp)
        .height(height)
        .border(1.dp, androidx.compose.ui.graphics.Color.Gray),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            textAlign = TextAlign.Center )
    }
}