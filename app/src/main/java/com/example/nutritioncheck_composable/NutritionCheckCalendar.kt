package com.example.nutritioncheck_composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionCheckCalendarLayout() {
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false
        )

        Text(
            text = SimpleDateFormat(
                "yyyy년 mm월 dd일",
                Locale.getDefault()
            ).format(Date(datePickerState.selectedDateMillis ?: 0))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCheckCalendar() {
    NutritionCheckCalendarLayout()
}