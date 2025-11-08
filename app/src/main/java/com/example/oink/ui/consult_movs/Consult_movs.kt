package com.example.oink.ui.consult_movs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Consult_movs_view(){
    var selectedDate by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ){
        CalendarVisible(selectedDate = selectedDate) { fecha ->
            selectedDate = fecha
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarVisible(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null // Ninguna fecha seleccionada al inicio
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            onDateSelected(formatter.format(Date(millis)))
        }
    }
    DatePicker(
        state = datePickerState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    )

    if (selectedDate.isNotEmpty()) {
        Text(
            text = "Fecha seleccionada: $selectedDate",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray
        )
    }


}


@Preview
@Composable
fun preview_vi(){
    Consult_movs_view()
}