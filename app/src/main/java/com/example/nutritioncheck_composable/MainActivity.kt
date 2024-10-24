package com.example.nutritioncheck_composable

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutritionNavigator()
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
        composable("NutritionAdd") {
            NutritionAddLayout()
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