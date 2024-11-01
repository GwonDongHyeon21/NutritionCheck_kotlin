package com.example.nutritioncheck_composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutritioncheck_composable.chart.nutritionChartData

@Composable
fun NutritionTodayChartLayout() {
    val nutritionTodayData = nutritionChartData(breakfastFoodList, lunchFoodList, dinnerFoodList)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .padding(start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            val height =
                ((LocalConfiguration.current.screenHeightDp - 40) / nutritionTodayData.size) * 0.5
            nutritionTodayData.forEach { (label, values) ->
                NutritionTodayChart(height, label, values.first, values.second)
            }
        }
    }
}

@Composable
fun NutritionTodayChart(height: Double, label: String, value: Float, maxValue: Float) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .align(Alignment.CenterStart),
        )

        Canvas(
            modifier = Modifier
                .height(height.dp)
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterEnd)
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

        Text(
            text = "${value.toInt()}/${maxValue.toInt()}" +
                    " (${(value / maxValue * 100).toInt()}%)",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionChart() {
    NutritionTodayChartLayout()
}