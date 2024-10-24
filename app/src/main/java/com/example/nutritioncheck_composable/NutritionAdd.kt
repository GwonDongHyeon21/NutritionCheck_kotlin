package com.example.nutritioncheck_composable

import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.nutritioncheck_composable.api.nutritionInfo
import com.example.nutritioncheck_composable.model.NutritionDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var foodList = mutableListOf<NutritionDataModel>()

@Composable
fun NutritionAddLayout() {
    var isDialogState by remember { mutableStateOf(false) }

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
                    .border(0.dp, Color.Black, RoundedCornerShape(16.dp)),
            ) {

            }

            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize(0.1f)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        isDialogState = true
                    },
                tint = Color(100, 60, 180, 255),
            )
        }

        Button(
            modifier = Modifier
                .padding(bottom = 60.dp)
                .align(Alignment.BottomCenter),
            onClick = { /*TODO*/ }
        ) {
            Text(text = "저장")
        }

        if (isDialogState)
            NutritionDataDialog()
    }
}

@Composable
fun NutritionDataDialog() {
    var isDialogVisible by remember { mutableStateOf(true) }
    var text by remember { mutableStateOf("") }
    var isEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (isDialogVisible) {
        Dialog(
            onDismissRequest = {
                isDialogVisible = false
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Row {
                        TextField(
                            value = text,
                            onValueChange = {
                                if (it.length in 1..10) {
                                    text = it
                                    isEnabled = true
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
                        )

                        Button(
                            onClick = {
                                isLoading = false
                                isEnabled = false
                                coroutineScope.launch(Dispatchers.IO) {
                                    nutritionInfo(text)
                                    isLoading = true
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
                                Text(text = food.foodName)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "잠시만 기다려주세요.",
                                style = TextStyle(color = Color.Gray)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionAdd() {
    NutritionAddLayout()
}