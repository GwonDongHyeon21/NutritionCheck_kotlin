package com.example.nutritioncheck_composable.chart

import com.example.nutritioncheck_composable.model.NutritionDataModel

fun nutritionChartData(
    breakfastList: List<NutritionDataModel>,
    lunchList: List<NutritionDataModel>,
    dinnerList: List<NutritionDataModel>
): List<Pair<String, Pair<Float, Float>>> {
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
    val list = mutableListOf(breakfastList, lunchList, dinnerList)
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

    val nutritionData = listOf(
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

    return nutritionData
}
