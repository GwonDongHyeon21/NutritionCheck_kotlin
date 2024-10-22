package com.example.nutritioncheck_composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionCalendarLayout() {
    val datePickerState = rememberDatePickerState()
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = true,
            modifier = Modifier.clickable {
                coroutineScope.launch(Dispatchers.IO) {
                    isLoading = false
                    //data를 불러오는 작업 필요
                    isLoading = true
                }
            }
        )

//        if (isLoading) {
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
//        } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "날짜를 선택하세요.")
        }
//        }

//        Text(
//            text = SimpleDateFormat(
//                "yyyy년 mm월 dd일",
//                Locale.getDefault()
//            ).format(Date(datePickerState.selectedDateMillis ?: 0))
//        )
    }
}

@Composable
fun DateNutritionChart(label: String, value: Float, maxValue: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .width(30.dp)
                .weight(1f)
                .shadow(10.dp)
                .background(Color(230, 230, 230, 255))
        ) {
            val barHeight =
                if (value > maxValue) {
                    size.height
                } else {
                    size.height * (value / maxValue)
                }
            drawRect(
                color = Color.Blue,
                topLeft = Offset(0f, size.height - barHeight),
                size = Size(size.width, barHeight),
            )
        }

        Text(
            text = label,
            modifier = Modifier
                .padding(top = 5.dp),
        )

        val nutritionPercentage = (value / maxValue * 100).toInt()
        Text(
            text = "($nutritionPercentage%)",
            modifier = Modifier
                .padding(top = 5.dp),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNutritionCalendar() {
    NutritionCalendarLayout()
}