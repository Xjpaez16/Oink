package com.example.oink.ui.consult_movs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.oink.ui.components.BottomNavBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Consult_movs_view(
    navController: NavController // 1. Recibimos el controlador necesario para la BottomBar
) {
    var selectedDate by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFF8FAFF)
    ) { innerPadding -> // 2. Usamos el padding del sistema

        // Usamos Column para apilar verticalmente el calendario y el resultado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 3. Aplicamos padding para no chocar con la navbar
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalendarVisible(selectedDate = selectedDate) { fecha ->
                selectedDate = fecha
            }
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
        initialSelectedDateMillis = null
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            // Ojo: Sumamos un offset si la fecha sale un día antes por temas de zona horaria,
            // pero para visualización simple esto suele bastar.
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            onDateSelected(formatter.format(Date(millis)))
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        if (selectedDate.isNotEmpty()) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Text(
                    text = "Fecha seleccionada: $selectedDate",
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF1565C0)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConsultMovs() {
    // Creamos un controlador falso para la vista previa
    val navController = rememberNavController()
    Consult_movs_view(navController = navController)
}
