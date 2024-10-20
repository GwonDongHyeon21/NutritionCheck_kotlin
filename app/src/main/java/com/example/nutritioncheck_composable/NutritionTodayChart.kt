package com.example.nutritioncheck_composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp

@Composable
fun NutritionCheckTodayLayout() {
    val nutritionData = listOf(
        "열량" to (1500f to 2000f), // 2000kcal 기준
        "탄수화물" to (140f to 324f),
        "당류" to (20f to 100f),
        "단백질" to (14f to 55f),
        "지방" to (20f to 54f),
        "포화지방" to (20f to 15f),
        "나트륨" to (1000f to 2000f),
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        nutritionData.forEach { (label, values) ->
            NutritionCheckChart(label, values.first, values.second)
        }
    }
}

@Composable
fun NutritionCheckChart(label: String, value: Float, maxValue: Float) {
    Row(
        modifier = Modifier
            .padding(start = 30.dp, end = 20.dp, top = 20.dp)
            .height(30.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(0.16f),
        )
        Canvas(
            modifier = Modifier
                .height(30.dp)
                .weight(1f)
                .shadow(10.dp)
                .background(Color(230, 230, 230, 255))
        ) {
            val barWidth =
                if (value > maxValue) {
                    size.width
                } else {
                    size.width * (value / maxValue)
                }
            drawRect(
                color = Color.Blue,
                size = Size(barWidth, size.height),
            )
        }

        val nutritionPercentage = (value / maxValue * 100).toInt()
        Text(
            text = "${value.toInt()}/${maxValue.toInt()}($nutritionPercentage%)",
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionToday() {
    NutritionCheckTodayLayout()
}