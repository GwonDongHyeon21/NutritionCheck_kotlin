package com.example.nutritioncheck_composable

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutritioncheck_composable.api.nutritionInfo
import com.example.nutritioncheck_composable.database.addDataToFirebase
import com.example.nutritioncheck_composable.database.deleteDataToFirebase
import com.example.nutritioncheck_composable.loading.LoadingLayout
import com.example.nutritioncheck_composable.model.NutritionDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NutritionAddLayout(navController: NavController, meal: String, date: String) {
    val dateFoodList = remember {
        when (meal) {
            "아침" -> ValueSingleton.breakfastFoodList.toMutableList()
            "점심" -> ValueSingleton.lunchFoodList.toMutableList()
            "저녁" -> ValueSingleton.dinnerFoodList.toMutableList()
            else -> {
                mutableListOf()
            }
        }
    }
    var addList = remember { mutableListOf<NutritionDataModel>() }
    var isAddDialog by remember { mutableStateOf(false) }
    var isFoodDialog by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf(listOf<NutritionDataModel>()) }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            if (dateFoodList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth(0.7f)
                        .padding(bottom = 40.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                        .padding(20.dp),
                ) {
                    items(dateFoodList) { food ->
                        Text(
                            text = food.foodName,
                            modifier = Modifier.clickable {
                                selectedFood = listOf(food)
                                isFoodDialog = true
                            },
                        )
                    }
                }
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth(0.7f)
                        .padding(bottom = 40.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
                    color = Color.Transparent,
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "비어 있습니다.",
                            style = TextStyle(color = Color.Gray)
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
                        ValueSingleton.foodList = mutableListOf()
                        isAddDialog = true
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
                        "아침" -> ValueSingleton.breakfastFoodList.add(it)
                        "점심" -> ValueSingleton.lunchFoodList.add(it)
                        "저녁" -> ValueSingleton.dinnerFoodList.add(it)
                    }
                }
                addList = mutableListOf()
                Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            },
            enabled = addList.isNotEmpty()
        ) {
            Text(text = "저장")
        }

        if (isAddDialog)
            NutritionDataDialog(dateFoodList, addList) { isAddDialog = false }

        if (isFoodDialog)
            FoodDetailDialog(
                selectedFood = selectedFood.first(),
                meal = meal,
                dateFoodList = dateFoodList,
            ) { isFoodDialog = false }

    }
}

@Composable
fun NutritionDataDialog(
    dateFoodList: MutableList<NutritionDataModel>,
    addList: MutableList<NutritionDataModel>,
    stateUpdate: () -> Unit
) {
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
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Top
            ) {
                val keyboardController = LocalSoftwareKeyboardController.current

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
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "음식을 입력하세요.",
                                style = TextStyle(color = Color.Gray),
                            )
                        },
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (isEnabled) {
                                    isLoading = false
                                    isProgress = true
                                    isEnabled = false
                                    searchText = text
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                    coroutineScope.launch(Dispatchers.IO) {
                                        ValueSingleton.foodList = mutableListOf()
                                        nutritionInfo(text)
                                        isLoading = true
                                        isProgress = false
                                    }
                                }
                            }
                        )
                    )

                    Button(
                        onClick = {
                            isLoading = false
                            isProgress = true
                            isEnabled = false
                            searchText = text
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            coroutineScope.launch(Dispatchers.IO) {
                                ValueSingleton.foodList = mutableListOf()
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
                        items(ValueSingleton.foodList) { food ->
                            Text(
                                text = food.foodName,
                                modifier = Modifier.clickable {
                                    addList.add(food)
                                    dateFoodList.add(food)
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
fun FoodDetailDialog(
    selectedFood: NutritionDataModel,
    meal: String,
    dateFoodList: MutableList<NutritionDataModel>,
    stateUpdate: () -> Unit
) {
    val context = LocalContext.current
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

                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    deleteDataToFirebase(ValueSingleton.selectedDate, meal, selectedFood)
                    when (meal) {
                        "아침" -> ValueSingleton.breakfastFoodList.remove(selectedFood)
                        "점심" -> ValueSingleton.lunchFoodList.remove(selectedFood)
                        "저녁" -> ValueSingleton.dinnerFoodList.remove(selectedFood)
                    }
                    dateFoodList.remove(selectedFood)
                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    stateUpdate()
                }) {
                    Text(text = "삭제하기")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionAdd() {
    NutritionAddLayout(rememberNavController(), "", "")
}