package com.example.nutritioncheck_composable.database

import com.example.nutritioncheck_composable.ValueSingleton
import com.example.nutritioncheck_composable.model.NutritionDataModel

fun addDataToFirebase(
    date: String,
    meal: String,
    nutritionData: MutableList<NutritionDataModel>,
) {
    nutritionData.forEach {
        ValueSingleton.DB.child(ValueSingleton.uid).child(date).child(meal).push().setValue(it)
    }
}