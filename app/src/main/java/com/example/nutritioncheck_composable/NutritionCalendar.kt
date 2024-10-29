package com.example.nutritioncheck_composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutritioncheck_composable.chart.nutritionChartData
import com.example.nutritioncheck_composable.database.getDataFromFirebase
import com.example.nutritioncheck_composable.loading.LoadingLayout
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionCalendarLayout() {
    val datePickerState = rememberDatePickerState()
    var isLoading by remember { mutableStateOf(true) }
    var nutritionDateChart by remember { mutableStateOf(listOf<Pair<String, Pair<Float, Float>>>()) }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        val formattedDate = datePickerState.selectedDateMillis?.let {
            SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(it)
        }
        isLoading = true
        getDataFromFirebase(formattedDate.toString()) {
            nutritionDateChart =
                nutritionChartData(it[0].toList(), it[1].toList(), it[2].toList()).toMutableList()
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DatePicker(
            title = null,
            showModeToggle = false,
            state = datePickerState,
            modifier = Modifier
                .padding(top = 20.dp)
        )

        if (isLoading) {
            Column(
                modifier = Modifier.padding(bottom = 40.dp)
            ) { LoadingLayout() }
        } else {
            if (datePickerState.selectedDateMillis != null) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 40.dp, end = 40.dp, bottom = 80.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val width =
                        ((LocalConfiguration.current.screenWidthDp - 80) / nutritionDateChart.size) * 0.8
                    nutritionDateChart.forEach { (label, values) ->
                        NutritionDateChart(width, label, values.first, values.second)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "날짜를 선택하세요.",
                        modifier = Modifier.padding(bottom = 40.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun NutritionDateChart(width: Double, label: String, value: Float, maxValue: Float) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Canvas(
            modifier = Modifier
                .width(width.dp)
                .fillMaxHeight(0.6f)
                .shadow(10.dp)
                .background(Color(230, 230, 230, 255))
        ) {
            var isColor = true
            var barHeight = size.height
            if (value > maxValue) {
                isColor = false
            } else {
                barHeight *= (value / maxValue)
            }
            drawRect(
                color = if (isColor) Color.Blue else Color.Red,
                topLeft = Offset(0f, size.height - barHeight),
                size = Size(size.width, barHeight),
            )
        }

        Text(
            text = label,
            modifier = Modifier.width(10.dp),
            style = TextStyle(fontSize = 12.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionCalendar() {
    NutritionCalendarLayout()
}