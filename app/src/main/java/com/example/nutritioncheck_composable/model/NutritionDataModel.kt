package com.example.nutritioncheck_composable.model

data class NutritionDataModel(
    val foodName: String,
    val calories: String,       //에너지(kcal)
    val carbohydrate: String,   //탄수화물(g)
    val sugar: String,          //당류(g)
    val dietaryFiber: String,   //식이섬유(g)
    val protein: String,        //단백질(g)
    val province: String,       //지방(g)
    val saturatedFat: String,   //포화지방산(g)
    val cholesterol: String,    //콜레스테롤(mg)
    val sodium: String,         //나트륨(mg)
    val potassium: String,      //칼륨(mg)
    val vitaminA: String,       //비타민 A(μg)
    val vitaminC: String,       //비타민 C(mg)
)