package com.example.nutritioncheck_composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NutritionCheckMainLayout() {
    var text by remember { mutableStateOf("") }

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "아침",
                modifier = Modifier.wrapContentSize(Alignment.Center),
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(start = 10.dp)
                    .shadow(4.dp)
                    .clickable {

                    },
                placeholder = { Text("아침 식사") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(240, 240, 240, 255),
                    unfocusedContainerColor = Color(240, 240, 240, 255),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "점심",
                modifier = Modifier.wrapContentSize(Alignment.Center),
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(start = 10.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text("점심 식사") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "저녁",
                modifier = Modifier.wrapContentSize(Alignment.Center),
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(start = 10.dp)
                    .shadow(4.dp),
                placeholder = { Text("저녁 식사") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(240, 240, 240, 255),
                    unfocusedContainerColor = Color(240, 240, 240, 255),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionCheckMain() {
    NutritionCheckMainLayout()
}