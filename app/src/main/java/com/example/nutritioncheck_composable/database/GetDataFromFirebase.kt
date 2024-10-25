package com.example.nutritioncheck_composable.database

import android.util.Log
import com.example.nutritioncheck_composable.DB
import com.example.nutritioncheck_composable.model.NutritionDataModel
import com.example.nutritioncheck_composable.uid

fun getDataFromFirebase(callback: (MutableList<NutritionDataModel>) -> Unit) {
    val nutritionDataInfo = mutableListOf<NutritionDataModel>()
    DB.child(uid).get().addOnSuccessListener { dataSnapshot ->
//        for (dataDate in dataSnapshot.children) {
//            for (data in dataDate.children) {
//                val nutritionData = NutritionDataModel(
//                    foodName = data.child("foodName").getValue(String::class.java) ?: "",
//                    calories = data.child("foodName").getValue(String::class.java) ?: "",
//                    carbohydrate = data.child("carbohydrate").getValue(String::class.java) ?: "",
//                    sugar = data.child("sugar").getValue(String::class.java)
//                        ?: "",
//                    dietaryFiber = data.child("dietaryFiber").getValue(String::class.java)
//                        ?: "",
//                    protein = data.child("protein").getValue(String::class.java)
//                        ?: "",
//                    province = data.child("province").getValue(String::class.java) ?: "",
//                    saturatedFat = data.child("saturatedFat").getValue(String::class.java)
//                        ?: "",
//                    cholesterol = data.child("cholesterol").getValue(String::class.java)
//                        ?: "",
//                    sodium = data.child("sodium").getValue(String::class.java)
//                        ?: "",
//                    potassium = data.child("sodium").getValue(String::class.java)
//                        ?: "",
//                    vitaminA = data.child("vitaminA").getValue(String::class.java)
//                        ?: "",
//                    vitaminC = data.child("vitaminC").getValue(String::class.java)
//                        ?: "",
//                )
//                nutritionDataInfo.add(nutritionData)
//            }
//        }
        callback(nutritionDataInfo)
    }.addOnFailureListener {
        callback(nutritionDataInfo)
    }
}