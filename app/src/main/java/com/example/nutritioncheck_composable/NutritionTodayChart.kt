package com.example.nutritioncheck_composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nutritioncheck_composable.chart.nutritionChart

@Composable
fun NutritionTodayChartLayout() {
    val nutritionTodayData = nutritionChart(breakfastFoodList, lunchFoodList, dinnerFoodList)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            nutritionTodayData.forEach { (label, values) ->
                NutritionTodayChart(label, values.first, values.second)
            }
        }
    }
}

@Composable
fun NutritionTodayChart(label: String, value: Float, maxValue: Float) {
    Column {
        Row(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
        ) {
            Text(
                text = label,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(0.2f),
            )

            Canvas(
                modifier = Modifier
                    .height(25.dp)
                    .weight(1f)
                    .shadow(10.dp)
                    .background(Color(230, 230, 230, 255))
            ) {
                var isColor = true
                var barWidth = size.width
                if (value > maxValue) {
                    isColor = false
                } else {
                    barWidth *= (value / maxValue)
                }
                drawRect(
                    color = if (isColor) Color.Blue else Color.Red,
                    size = Size(barWidth, size.height),
                )
            }
        }

        val nutritionPercentage = (value / maxValue * 100).toInt()
        Text(
            text = "${value.toInt()}/${maxValue.toInt()}($nutritionPercentage%)",
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 5.dp, end = 25.dp)
        )
    }
}