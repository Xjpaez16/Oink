package com.example.oink.ui.consult_movs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource // Importación necesaria
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.oink.R // Importación necesaria para acceder a los strings
import com.example.oink.ui.components.BottomNavBar
import com.example.oink.ui.components.MovementItem
import com.example.oink.viewmodel.AuthViewModel
import com.example.oink.viewmodel.ConsultMovsViewModel
import java.util.Date
import java.util.TimeZone

@Composable
fun Consult_movs_view(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    viewModel: ConsultMovsViewModel = viewModel()
) {
    val currentUser = authViewModel.currentUser.value
    val movements by viewModel.movements.collectAsState()
    val isLoading by viewModel.isLoading
    val searchPerformed by viewModel.searchPerformed

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.White
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Calendario y título
            item {
                Spacer(modifier = Modifier.height(24.dp))

                // CORREGIDO: Título con stringResource
                Text(
                    text = stringResource(R.string.consult_title),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D3685)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // CORREGIDO: Subtítulo con stringResource
                Text(
                    text = stringResource(R.string.consult_subtitle),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                CalendarVisible { selectedDate ->
                    currentUser?.id?.let { userId ->
                        viewModel.loadMovementsForDate(userId, selectedDate)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }


            if (isLoading) {
                item {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            } else {
                if (movements.isNotEmpty()) {
                    // Usamos `items` para un rendimiento óptimo
                    items(movements, key = { it.id }) { movement ->
                        MovementItem(movement = movement)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else if (searchPerformed) {
                    item {
                        // CORREGIDO: Mensaje de "sin resultados" con stringResource
                        Text(
                            text = stringResource(R.string.consult_no_results),
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarVisible(
    onDateSelected: (Date) -> Unit
) {

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    // Observador que se activa cada vez que se elige una nueva fecha.
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val offset = TimeZone.getDefault().getOffset(millis)
            onDateSelected(Date(millis - offset))
        }
    }


    DatePicker(
        state = datePickerState,
        showModeToggle = true,
        colors = DatePickerDefaults.colors(
            selectedDayContainerColor = Color(0xFF2997FD),
            todayDateBorderColor = Color(0xFF2997FD),
            todayContentColor = Color(0xFF0D3685)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewConsultMovs() {
    val navController = rememberNavController()
    Consult_movs_view(navController = navController)
}
