package com.example.nutritioncheck_composable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.nutritioncheck_composable.database.getDataFromFirebase
import com.example.nutritioncheck_composable.ui.loading.LoadingLayout
import com.example.nutritioncheck_composable.model.NutritionDataModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Locale

object ValueSingleton {
    var uid: String = ""
    val DB = Firebase.database.reference

    var selectedDate: String = SimpleDateFormat(
        "yyyy년 MM월 dd일",
        Locale.getDefault()
    ).format(System.currentTimeMillis())

    var breakfastFoodList = mutableListOf<NutritionDataModel>()
    var lunchFoodList = mutableListOf<NutritionDataModel>()
    var dinnerFoodList = mutableListOf<NutritionDataModel>()

    var foodList = mutableListOf<NutritionDataModel>()
}

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoadingLayout()
        }

        auth = FirebaseAuth.getInstance()
        signInAnonymously()

        getDataFromFirebase(ValueSingleton.selectedDate) {
            ValueSingleton.breakfastFoodList = it[0].toMutableList()
            ValueSingleton.lunchFoodList = it[1].toMutableList()
            ValueSingleton.dinnerFoodList = it[2].toMutableList()

            setContent {
                LayoutNavigator()
            }
        }
    }

    private fun signInAnonymously() {
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        ValueSingleton.uid = auth.currentUser?.uid ?: ""
                    }
                }
        } else {
            ValueSingleton.uid = auth.currentUser?.uid ?: ""
        }
    }
}
