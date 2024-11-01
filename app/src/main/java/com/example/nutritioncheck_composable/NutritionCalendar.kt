package com.example.nutritioncheck_composable

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.nutritioncheck_composable.chart.nutritionChartData
import com.example.nutritioncheck_composable.database.deleteDataToFirebase
import com.example.nutritioncheck_composable.database.getDataFromFirebase
import com.example.nutritioncheck_composable.loading.LoadingLayout
import com.example.nutritioncheck_composable.model.NutritionDataModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionCalendarLayout() {
    val datePickerState = rememberDatePickerState()
    var isLoading by remember { mutableStateOf(true) }
    var nutritionDateChart by remember { mutableStateOf(listOf<Pair<String, Pair<Float, Float>>>()) }
    var isDateDetail by remember { mutableStateOf(false) }
    var foodList by remember { mutableStateOf(listOf(listOf<NutritionDataModel>())) }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        val formattedDate = datePickerState.selectedDateMillis?.let {
            SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(it)
        }
        isLoading = true
        getDataFromFirebase(formattedDate.toString()) {
            nutritionDateChart =
                nutritionChartData(it[0].toList(), it[1].toList(), it[2].toList()).toMutableList()
            isLoading = false

            foodList = listOf(it[0].toMutableList(), it[1].toMutableList(), it[2].toMutableList())
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DatePicker(
            title = null,
            showModeToggle = false,
            state = datePickerState,
            modifier = Modifier
                .padding(top = 20.dp)
        )

        if (isLoading) {
            Column(
                modifier = Modifier.padding(bottom = 40.dp)
            ) { LoadingLayout() }
        } else {
            if (datePickerState.selectedDateMillis != null) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 40.dp, end = 40.dp, bottom = 80.dp)
                        .clickable { isDateDetail = true },
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val width =
                        ((LocalConfiguration.current.screenWidthDp - 80) / nutritionDateChart.size) * 0.8
                    nutritionDateChart.forEach { (label, values) ->
                        NutritionDateChart(width, label, values.first, values.second)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "날짜를 선택하세요.",
                        modifier = Modifier.padding(bottom = 40.dp),
                    )
                }
            }
        }
    }

    if (isDateDetail)
        DateDetailDialog(
            foodList = foodList,
            stateUpdate = { isDateDetail = false },
        )
}

@Composable
fun NutritionDateChart(width: Double, label: String, value: Float, maxValue: Float) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Canvas(
            modifier = Modifier
                .width(width.dp)
                .fillMaxHeight(0.6f)
                .shadow(10.dp)
                .background(Color(230, 230, 230, 255))
        ) {
            var isColor = true
            var barHeight = size.height
            if (value > maxValue) {
                isColor = false
            } else {
                barHeight *= (value / maxValue)
            }
            drawRect(
                color = if (isColor) Color.Blue else Color.Red,
                topLeft = Offset(0f, size.height - barHeight),
                size = Size(size.width, barHeight),
            )
        }

        Text(
            text = label,
            modifier = Modifier.width(10.dp),
            style = TextStyle(fontSize = 12.sp)
        )
    }
}


@Composable
fun DateDetailDialog(
    foodList: List<List<NutritionDataModel>>,
    stateUpdate: () -> Unit,
) {
    var isFoodDetail by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf(listOf<NutritionDataModel>()) }

    Dialog(
        onDismissRequest = { stateUpdate() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                    .weight(1f)
                    .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                items(foodList[0]) {
                    Text(
                        text = it.foodName,
                        modifier = Modifier.clickable {
                            selectedFood = listOf(it)
                            isFoodDetail = true
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                    .weight(1f)
                    .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                items(foodList[1]) {
                    Text(
                        text = it.foodName,
                        modifier = Modifier.clickable {
                            selectedFood = listOf(it)
                            isFoodDetail = true
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
                    .weight(1f)
                    .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                items(foodList[2]) {
                    Text(
                        text = it.foodName,
                        modifier = Modifier.clickable {
                            selectedFood = listOf(it)
                            isFoodDetail = true
                        }
                    )
                }
            }
        }
    }

    if (isFoodDetail)
        DateFoodDetailDialog(
            selectedFood = selectedFood.first(),
            stateUpdate = { isFoodDetail = false },
        )
}


@Composable
fun DateFoodDetailDialog(
    selectedFood: NutritionDataModel,
    stateUpdate: () -> Unit
) {
    val foodAttributes = listOf(
        "이름" to selectedFood.foodName,
        "칼로리" to selectedFood.calories,
        "탄수화물" to selectedFood.carbohydrate,
        "당류" to selectedFood.sugar,
        "식이섬유" to selectedFood.dietaryFiber,
        "단백질" to selectedFood.protein,
        "지방" to selectedFood.province,
        "포화지방" to selectedFood.saturatedFat,
        "콜레스테롤" to selectedFood.cholesterol,
        "나트륨" to selectedFood.sodium,
        "칼륨" to selectedFood.potassium,
        "비타민A" to selectedFood.vitaminA,
        "비타민C" to selectedFood.vitaminC,
        "1회 섭취참고량" to selectedFood.amountPer,
        "식품중량" to selectedFood.amountAll,
        "업체명" to selectedFood.makerName,
    )

    Dialog(
        onDismissRequest = { stateUpdate() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                foodAttributes.forEach { (label, value) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = label)
                        Text(text = value)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionCalendar() {
    NutritionCalendarLayout()
}