package com.example.nutritioncheck_composable.database

import com.example.nutritioncheck_composable.ValueSingleton
import com.example.nutritioncheck_composable.model.NutritionDataModel

fun deleteDataToFirebase(date: String, meal: String, selectedFood: NutritionDataModel) {
    ValueSingleton.DB.child(ValueSingleton.uid).child(date).child(meal).get()
        .addOnSuccessListener { dataSnapshot ->
            dataSnapshot.children.forEach {
                val keyValue = it.child("key").getValue(Long::class.java)
                if (keyValue == selectedFood.key)
                    it.ref.removeValue()
            }
        }
}