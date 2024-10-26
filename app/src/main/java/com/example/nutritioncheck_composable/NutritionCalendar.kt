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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionCalendarLayout() {
    val datePickerState = rememberDatePickerState()
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("날짜를 선택해주세요.") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DatePicker(
            title = null,
            state = datePickerState,
            showModeToggle = false,
            modifier = Modifier
                .padding(top = 10.dp)
        )
        LaunchedEffect(datePickerState.selectedDateMillis) {
            if (datePickerState.selectedDateMillis != null) {
//                coroutineScope.launch(Dispatchers.IO) {
                    text = "잠시만 기다려주세요."
                    isLoading = false
                    // 데이터 불러오기
                    isLoading = true
//                }
            }
        }

        if (isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, end = 40.dp, bottom = 80.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                nutritionData.forEach { (label, values) ->
                    DateNutritionChart(label, values.first, values.second)
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(bottom = 40.dp),
                )
            }
        }
    }
}

@Composable
fun DateNutritionChart(label: String, value: Float, maxValue: Float) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Canvas(
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight(0.7f)
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
            modifier = Modifier
                .width(10.dp)
                .padding(top = 5.dp),
            style = TextStyle(fontSize = 12.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionCalendar() {
    NutritionCalendarLayout()
}