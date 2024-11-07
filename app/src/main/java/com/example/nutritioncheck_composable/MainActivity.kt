package com.example.nutritioncheck_composable

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutritioncheck_composable.database.getDataFromFirebase
import com.example.nutritioncheck_composable.loading.LoadingLayout
import com.example.nutritioncheck_composable.model.NutritionDataModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import kotlinx.coroutines.launch
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

@Composable
fun LayoutNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "TabLayout") {
        composable("TabLayout") {
            TabLayout(navController)
        }
        composable("NutritionToday") {
            NutritionTodayLayout(navController)
        }
        composable(
            route = "NutritionAdd/{meal}/{selectedDate}",
            arguments = listOf(
                navArgument("meal") { type = NavType.StringType },
                navArgument("selectedDate") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val meal = backStackEntry.arguments?.getString("meal").toString()
            val date = backStackEntry.arguments?.getString("selectedDate").toString()

            NutritionAddLayout(navController, meal, date)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TabLayout(navController: NavController) {
    val tabs = listOf(Icons.Filled.Check, Icons.AutoMirrored.Filled.List, Icons.Filled.DateRange)
    val pagerState = rememberPagerState(0, 0f) { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color(230, 220, 255, 255),
            ) {
                tabs.forEachIndexed { index, icon ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        icon = { Icon(imageVector = icon, contentDescription = "") }
                    )
                }
            }
        }
    ) {
        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> NutritionTodayLayout(navController)
                1 -> NutritionTodayChartLayout()
                2 -> NutritionCalendarLayout()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainActivity() {
    LayoutNavigator()
}