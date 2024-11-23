package com.example.nutritioncheck_composable

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nutritioncheck_composable.ui.NutritionAddLayout
import com.example.nutritioncheck_composable.ui.NutritionTodayLayout
import com.example.nutritioncheck_composable.ui.TabLayout

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