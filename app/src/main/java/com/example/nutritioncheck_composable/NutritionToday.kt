package com.example.nutritioncheck_composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun NutritionTodayLayout(navController: NavController) {
    Column(
        modifier = Modifier.padding(start = 40.dp, end = 40.dp, bottom = 60.dp),
    ) {
        Text(
            text = "영양 체크",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.BottomCenter)
                .weight(1f),
            style = TextStyle(fontSize = 30.sp),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
        ) {
            Text(
                text = "아침",
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterStart),
                style = TextStyle(fontSize = 20.sp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.7f)
                    .align(Alignment.Center)
                    .shadow(10.dp, RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate("NutritionAdd")
                    },
            ) {
                //데이터 가져오기
                val breakfastData = emptyList<String>()
                if (breakfastData.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "비어 있습니다.",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            style = TextStyle(color = Color.Gray)
                        )
                    }
                }else{
                    //데이터 보여주기
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
        ) {
            Text(
                text = "점심",
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterStart),
                style = TextStyle(fontSize = 20.sp)
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.7f)
                    .align(Alignment.Center)
                    .shadow(10.dp, RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate("NutritionAdd")
                    },
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "비어 있습니다.",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(color = Color.Gray)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
        ) {
            Text(
                text = "저녁",
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterStart),
                style = TextStyle(fontSize = 20.sp)
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.7f)
                    .align(Alignment.Center)
                    .shadow(10.dp, RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate("NutritionAdd")
                    },
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "비어 있습니다.",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(color = Color.Gray)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionToday() {
    NutritionTodayLayout(rememberNavController())
}