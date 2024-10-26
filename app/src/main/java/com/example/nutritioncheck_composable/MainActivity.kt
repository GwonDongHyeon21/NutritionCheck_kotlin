package com.example.nutritioncheck_composable

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutritioncheck_composable.database.getDataFromFirebase
import com.example.nutritioncheck_composable.model.NutritionDataModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import java.time.LocalDate

val nutritionData = listOf(
    "열량" to (1500f to 2000f), // 2000kcal 기준
    "탄수화물" to (140f to 324f),
    "당류" to (20f to 100f),
    "식이섬유" to (20f to 25f),
    "단백질" to (14f to 55f),
    "지방" to (20f to 54f),
    "포화지방" to (20f to 15f),
    "콜레스테롤" to (200f to 300f),
    "나트륨" to (1000f to 2000f),
    "칼륨" to (1000f to 3500f),
    "비타민A" to (100f to 700f),
    "비타민C" to (90f to 100f),
)

object ValueSingleton {
    var uid: String = ""
}

val db = Firebase.database
val DB = db.reference
val uid = ValueSingleton.uid

var breakfastFoodList = mutableListOf<NutritionDataModel>()
var lunchFoodList = mutableListOf<NutritionDataModel>()
var dinnerFoodList = mutableListOf<NutritionDataModel>()

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private val date = LocalDate.now().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color.Gray,
                    trackColor = Color(100, 60, 180, 255),
                )
            }
        }

        auth = FirebaseAuth.getInstance()
        signInAnonymously()

        breakfastFoodList = mutableListOf()
        lunchFoodList = mutableListOf()
        dinnerFoodList = mutableListOf()

        getDataFromFirebase(date) {
            if (it) {
                setContent {
                    NutritionNavigator()
                }
            } else {
                //데이터를 불러오지 못한 경우 구현
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
fun NutritionNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "TabLayout") {
        composable("TabLayout") {
            TabLayout(navController)
        }
        composable("NutritionToday") {
            NutritionTodayLayout(navController)
        }
        composable(
            route = "NutritionAdd/{meal}",
            arguments = listOf(navArgument("meal") { type = NavType.StringType })
        ) { backStackEntry ->
            val meal = backStackEntry.arguments?.getString("meal")

            NutritionAddLayout(meal.toString())
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TabLayout(navController: NavController) {
    val tabs = listOf(
        Icons.Filled.Check,
        Icons.AutoMirrored.Filled.List,
        Icons.Filled.DateRange,
//        Icons.Filled.AccountCircle
    )
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
//                3 -> NutritionUserInfoLayout()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTabLayout() {
    TabLayout(rememberNavController())
}