package com.example.nutritioncheck_composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nutritioncheck_composable.database.getDataFromFirebase
import com.example.nutritioncheck_composable.model.NutritionDataModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NutritionTodayLayout(navController: NavController) {
    var isDialog by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(selectedDate) }

    LaunchedEffect(selectedDate) {
        date = selectedDate
    }

    Column(
        modifier = Modifier
            .padding(start = 40.dp, top = 40.dp, end = 40.dp, bottom = 80.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "영양 체크",
                    style = TextStyle(fontSize = 30.sp),
                )
                Text(
                    text = date,
                    modifier = Modifier.padding(top = 5.dp),
                )
            }

            IconButton(
                onClick = { isDialog = true },
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterEnd),
                colors = IconButtonDefaults.iconButtonColors(Color(230, 220, 255, 255))
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "",
                    tint = Color(100, 60, 180, 255)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MealRow("아침", breakfastFoodList, navController)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MealRow("점심", lunchFoodList, navController)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            MealRow("저녁", dinnerFoodList, navController)
        }
    }

    if (isDialog)
        DatePickerDialog(stateUpdate = { isDialog = false })
}

@Composable
fun MealRow(
    mealType: String,
    foodList: MutableList<NutritionDataModel>,
    navController: NavController,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(0.8f)
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .clickable {
                addList = mutableListOf()
                dateFoodList = foodList.toMutableList()
                navController.navigate("NutritionAdd/$mealType/$selectedDate")
            },
    ) {
        if (foodList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = mealType,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = TextStyle(color = Color.Gray)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                items(foodList) { food ->
                    Text(text = food.foodName)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(stateUpdate: () -> Unit) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = { stateUpdate() },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedDate = datePickerState.selectedDateMillis?.let {
                        SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(it)
                    }.toString()
                    getDataFromFirebase(selectedDate) {
                        breakfastFoodList = it[0].toMutableList()
                        lunchFoodList = it[1].toMutableList()
                        dinnerFoodList = it[2].toMutableList()
                        stateUpdate()
                    }
                }
            ) { Text(text = "확인") }
        },
        dismissButton = { TextButton(onClick = { stateUpdate() }) { Text(text = "취소") } },
        modifier = Modifier.padding(10.dp)
    ) {
        DatePicker(
            title = null,
            showModeToggle = false,
            state = datePickerState,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}