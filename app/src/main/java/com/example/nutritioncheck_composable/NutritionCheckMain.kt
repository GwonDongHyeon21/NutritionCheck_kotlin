package com.example.nutritioncheck_composable

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import kotlinx.coroutines.launch

class NutritionCheckMain : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabLayout()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout() {
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
                0 -> NutritionCheckMainLayout()
                1 -> NutritionCheckTodayLayout()
                2 -> NutritionCheckCalendarLayout()
            }
        }
    }
}
