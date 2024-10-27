package com.example.nutritioncheck_composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.nutritioncheck_composable.api.nutritionInfo
import com.example.nutritioncheck_composable.database.addDataToFirebase
import com.example.nutritioncheck_composable.loading.LoadingLayout
import com.example.nutritioncheck_composable.model.NutritionDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var foodList = mutableListOf<NutritionDataModel>()

@Composable
fun NutritionAddLayout(meal: String, date: String) {
    var isAddDialog by remember { mutableStateOf(false) }
    var isFoodDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedFood by remember { mutableStateOf(listOf<NutritionDataModel>()) }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 40.dp)
                    .border(0.dp, Color.Black, RoundedCornerShape(16.dp))
                    .padding(20.dp),
            ) {
                if (isLoading) {
                    items(list) { food ->
                        Text(
                            text = food.foodName,
                            modifier = Modifier.clickable {
                                selectedFood = listOf(food)
                                isFoodDialog = true
                            },
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize(0.1f)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        foodList = mutableListOf()
                        isAddDialog = true
                        isLoading = false
                    },
                tint = Color(100, 60, 180, 255),
            )
        }

        Button(
            modifier = Modifier
                .padding(bottom = 60.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                addDataToFirebase(date, meal, addList)
                addList.forEach {
                    when (meal) {
                        "아침" -> breakfastFoodList.add(it)
                        "점심" -> lunchFoodList.add(it)
                        "저녁" -> dinnerFoodList.add(it)
                    }
                }
                addList = mutableListOf()
                //Toast 메시지, 액션 효과, 뒤로 이동
            }
        ) {
            Text(text = "저장")
        }

        if (isAddDialog)
            NutritionDataDialog(
                stateUpdate = {
                    isLoading = true
                    isAddDialog = false
                }
            )

        if (isFoodDialog)
            FoodDialog(
                selectedFood = selectedFood,
                stateUpdate = { isFoodDialog = false }
            )
    }
}

@Composable
fun NutritionDataDialog(stateUpdate: () -> Unit) {
    var text by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isProgress by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var searchText = ""

    Dialog(
        onDismissRequest = { stateUpdate() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    TextField(
                        value = text,
                        onValueChange = {
                            if (it.isEmpty()) {
                                text = it
                                isEnabled = false
                            } else if (it.length <= 10) {
                                text = it
                                isEnabled = true
                                if (searchText == it)
                                    isEnabled = false
                            }
                        },
                        modifier = Modifier
                            .weight(1f),
                        placeholder = {
                            Text(
                                text = "음식을 입력하세요.",
                                style = TextStyle(color = Color.Gray),
                            )
                        },
                        maxLines = 1,
                        singleLine = true,
                    )

                    Button(
                        onClick = {
                            isLoading = false
                            isProgress = true
                            isEnabled = false
                            searchText = text
                            focusManager.clearFocus()
                            coroutineScope.launch(Dispatchers.IO) {
                                foodList = mutableListOf()
                                nutritionInfo(text)
                                isLoading = true
                                isProgress = false
                            }
                        },
                        enabled = isEnabled
                    ) {
                        Text(text = "검색")
                    }
                }

                if (isLoading) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(foodList) { food ->
                            Text(
                                text = food.foodName,
                                style = TextStyle(fontSize = 20.sp),
                                modifier = Modifier.clickable {
                                    addList.add(food)
                                    list.add(food)
                                    stateUpdate()
                                },
                            )
                        }
                    }
                } else {
                    if (isProgress) {
                        LoadingLayout()
                    }
                }
            }
        }
    }
}

@Composable
fun FoodDialog(selectedFood: List<NutritionDataModel>, stateUpdate: () -> Unit) {
    Dialog(
        onDismissRequest = { stateUpdate() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
//                Text(text = selectedFood.first().foodName)
//                Text(text = selectedFood.first().calories)
//                Text(text = selectedFood.first().carbohydrate)
//                Text(text = selectedFood.first().sugar)
//                Text(text = selectedFood.first().dietaryFiber)
//                Text(text = selectedFood.first().protein)
//                Text(text = selectedFood.first().province)
//                Text(text = selectedFood.first().saturatedFat)
//                Text(text = selectedFood.first().cholesterol)
//                Text(text = selectedFood.first().sodium)
//                Text(text = selectedFood.first().potassium)
//                Text(text = selectedFood.first().vitaminA)
//                Text(text = selectedFood.first().vitaminC)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionAdd() {
    NutritionAddLayout("", "")
}