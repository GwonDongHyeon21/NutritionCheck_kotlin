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
    val list = listOf(breakfastFoodList, lunchFoodList, dinnerFoodList)
//    val calories = list.flatten().map { it.calories.toFloat() }.sum()
//    val carbohydrate = list.flatten().map { it.carbohydrate.toFloat() }.sum()
//    val sugar = list.flatten().map { it.sugar.toFloat() }.sum()
//    val dietaryFiber = list.flatten().map { it.dietaryFiber.toFloat() }.sum()
//    val protein = list.flatten().map { it.protein.toFloat() }.sum()
//    val province = list.flatten().map { it.province.toFloat() }.sum()
//    val saturatedFat = list.flatten().map { it.saturatedFat.toFloat() }.sum()
//    val cholesterol = list.flatten().map { it.cholesterol.toFloat() }.sum()
//    val sodium = list.flatten().map { it.sodium.toFloat() }.sum()
//    val potassium = list.flatten().map { it.potassium.toFloat() }.sum()
//    val vitaminA = list.flatten().map { it.vitaminA.toFloat() }.sum()
//    val vitaminC = list.flatten().map { it.vitaminC.toFloat() }.sum()
    var calories = 0f
    var carbohydrate = 0f
    var sugar = 0f
    var dietaryFiber = 0f
    var protein = 0f
    var province = 0f
    var saturatedFat = 0f
    var cholesterol = 0f
    var sodium = 0f
    var potassium = 0f
    var vitaminA = 0f
    var vitaminC = 0f

    list.flatten().forEach { food ->
        calories += food.calories.toFloat()
        carbohydrate += food.carbohydrate.toFloat()
        sugar += food.sugar.toFloat()
        dietaryFiber += food.dietaryFiber.toFloat()
        protein += food.protein.toFloat()
        province += food.province.toFloat()
        saturatedFat += food.saturatedFat.toFloat()
        cholesterol += food.cholesterol.toFloat()
        sodium += food.sodium.toFloat()
        potassium += food.potassium.toFloat()
        vitaminA += food.vitaminA.toFloat()
        vitaminC += food.vitaminC.toFloat()
    }

    val nutritionTodayData = listOf(
        "열량" to (calories to 2000f),
        "탄수화물" to (carbohydrate to 324f),
        "당류" to (sugar to 100f),
        "식이섬유" to (dietaryFiber to 25f),
        "단백질" to (protein to 55f),
        "지방" to (province to 54f),
        "포화지방" to (saturatedFat to 15f),
        "콜레스테롤" to (cholesterol to 300f),
        "나트륨" to (sodium to 2000f),
        "칼륨" to (potassium to 3500f),
        "비타민A" to (vitaminA to 700f),
        "비타민C" to (vitaminC to 100f),
    )

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

@Preview(showBackground = true)
@Composable
fun PreviewNutritionTodayChart() {
    NutritionTodayChartLayout()
}