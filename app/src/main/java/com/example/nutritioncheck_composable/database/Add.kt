package com.example.nutritioncheck_composable.database

import com.example.nutritioncheck_composable.DB
import com.example.nutritioncheck_composable.model.NutritionDataModel
import com.example.nutritioncheck_composable.uid

fun addDataToFirebase(
    date: String,
    meal: String,
    nutritionData: MutableList<NutritionDataModel>,
) {
    nutritionData.forEach {
        DB.child(uid).child(date).child(meal).push().setValue(it)
    }
}