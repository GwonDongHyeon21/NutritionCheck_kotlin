package com.example.nutritioncheck_composable.database

import com.example.nutritioncheck_composable.ValueSingleton
import com.example.nutritioncheck_composable.model.NutritionDataModel

fun getDataFromFirebase(
    date: String,
    callback: (List<List<NutritionDataModel>>) -> Unit
) {
    val breakfastList = mutableListOf<NutritionDataModel>()
    val lunchList = mutableListOf<NutritionDataModel>()
    val dinnerList = mutableListOf<NutritionDataModel>()
    ValueSingleton.DB.child(ValueSingleton.uid).get().addOnSuccessListener { dataSnapshot ->
        for (dataDate in dataSnapshot.children) {
            if (dataDate.key == date) {
                for (dataMeal in dataDate.children) {
                    for (data in dataMeal.children) {
                        val nutritionData = NutritionDataModel(
                            foodName = data.child("foodName").getValue(String::class.java)
                                ?: "",
                            calories = data.child("calories").getValue(String::class.java)
                                ?: "",
                            carbohydrate = data.child("carbohydrate")
                                .getValue(String::class.java) ?: "",
                            sugar = data.child("sugar").getValue(String::class.java) ?: "",
                            dietaryFiber = data.child("dietaryFiber")
                                .getValue(String::class.java) ?: "",
                            protein = data.child("protein").getValue(String::class.java) ?: "",
                            province = data.child("province").getValue(String::class.java)
                                ?: "",
                            saturatedFat = data.child("saturatedFat")
                                .getValue(String::class.java) ?: "",
                            cholesterol = data.child("cholesterol").getValue(String::class.java)
                                ?: "",
                            sodium = data.child("sodium").getValue(String::class.java) ?: "",
                            potassium = data.child("potassium").getValue(String::class.java)
                                ?: "",
                            vitaminA = data.child("vitaminA").getValue(String::class.java)
                                ?: "",
                            vitaminC = data.child("vitaminC").getValue(String::class.java)
                                ?: "",
                        )

                        when (dataMeal.key) {
                            "아침" -> breakfastList.add(nutritionData)
                            "점심" -> lunchList.add(nutritionData)
                            "저녁" -> dinnerList.add(nutritionData)
                        }
                    }
                }
            }
        }
        callback(listOf(breakfastList, lunchList, dinnerList))
    }.addOnFailureListener {
        callback(emptyList())
    }
}