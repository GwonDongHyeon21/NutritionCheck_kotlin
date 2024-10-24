package com.example.nutritioncheck_composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun NutritionUserInfoLayout() {
    var nutritionWeekData = emptyList<Pair<String, Pair<Float, Float>>>()
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var selectedDate = LocalDate.now()

    LaunchedEffect(selectedDate) {
        coroutineScope.launch(Dispatchers.IO) {
            val monday = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val sunday = selectedDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
            nutritionWeekData = nutritionWeekData(monday.toString(), sunday.toString())
            isLoading = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, end = 40.dp, bottom = 60.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                nutritionWeekData.forEach { (label, values) ->
                    DateNutritionChart(label, values.first, values.second)
                }
            }
        } else {
            Text(text = "Loading...")
        }
    }
}

fun nutritionWeekData(monday: String, sunday: String): List<Pair<String, Pair<Float, Float>>> {
    //주간 데이터 불러오기
    val nutritionWeekData = listOf(
        "열량" to (2100f to 2000f), // 2000kcal 기준
        "탄수화물" to (140f to 324f),
        "당류" to (89f to 100f),
        "단백질" to (40f to 55f),
        "지방" to (60f to 54f),
        "포화지방" to (4f to 15f),
        "나트륨" to (1500f to 2000f),
    )

    return nutritionWeekData
}

@Composable
@Preview(showBackground = true)
fun PreviewNutritionUserInfo() {
    NutritionUserInfoLayout()
}