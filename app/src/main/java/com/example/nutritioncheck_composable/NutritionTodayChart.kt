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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NutritionTodayChartLayout() {
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
            nutritionData.forEach { (label, values) ->
                NutritionCheckChart(label, values.first, values.second)
            }
        }
    }
}

@Composable
fun NutritionCheckChart(label: String, value: Float, maxValue: Float) {
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

@Preview(showBackground = true)
@Composable
fun PreviewNutritionTodayChart() {
    NutritionTodayChartLayout()
}