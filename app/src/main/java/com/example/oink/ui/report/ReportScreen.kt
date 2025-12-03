package com.example.oink.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oink.viewmodel.ReportViewModel
import com.example.oink.viewmodel.AuthViewModel
import com.example.oink.viewmodel.ExpenseIncomeViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.oink.R
import com.example.oink.ui.components.BottomNavBar
import com.example.oink.ui.registerApp.DatePickerTextField
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ReportScreen(
    navController: NavController, // 1. Agregamos el parámetro necesario
    userName: String,
    viewModel: ReportViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    // Load data for current user
    val currentUser = authViewModel.getLoggedUser()
    val userId = currentUser?.id ?: ""

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) viewModel.loadReportForUser(userId)
    }

    // Formatter for the DatePickerTextField (dd/MM/yyyy)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Local copies for the read-only text fields
    var startText by remember { mutableStateOf(viewModel.startDate.format(formatter)) }
    var endText by remember { mutableStateOf(viewModel.endDate.format(formatter)) }

    // When the ViewModel updates start/end we reflect it locally
    LaunchedEffect(viewModel.startDate, viewModel.endDate) {
        startText = viewModel.startDate.format(formatter)
        endText = viewModel.endDate.format(formatter)
    }
    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.White
    ) { innerPadding -> // 2. Recibimos el padding del Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 3. Aplicamos el padding para evitar que la navbar tape el contenido
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            Text(
                text = stringResource(R.string.greeting_mr, userName),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C60E7)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.report_subtitle),
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.report_header),
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C60E7)
            )

            Spacer(Modifier.height(24.dp))

            // Fechas (Desde - Hasta)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    DatePickerTextField(
                        label = stringResource(R.string.label_from),
                        value = startText,
                        onValueChange = { new ->
                            // parse dd/MM/yyyy
                            try {
                                val parsed = LocalDate.parse(new, formatter)
                                viewModel.updateDateRange(parsed, viewModel.endDate)
                                viewModel.loadReportForRange(userId, parsed, viewModel.endDate)
                            } catch (e: Exception) { /* ignore parse errors */ }
                        }
                    )
                }

                Column {
                    DatePickerTextField(
                        label = stringResource(R.string.label_to),
                        value = endText,
                        onValueChange = { new ->
                            try {
                                val parsed = LocalDate.parse(new, formatter)
                                viewModel.updateDateRange(viewModel.startDate, parsed)
                                viewModel.loadReportForRange(userId, viewModel.startDate, parsed)
                            } catch (e: Exception) { /* ignore parse errors */ }
                        }
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Breakdown by category (simple visual)
            val categories = remember(viewModel.startDate, viewModel.endDate, viewModel.totalExpenses, viewModel.totalIncome) {
                // We don't have a map in the viewModel for breakdown, so compute from top categories when available.
                // For simplicity show the two top categories if present.
                listOfNotNull(viewModel.topExpenseCategory, viewModel.topIncomeCategory)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFEAF2FF), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        Text(text = stringResource(R.string.report_breakdown), color = Color(0xFF1C60E7), fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        if (viewModel.topExpenseCategory == null && viewModel.topIncomeCategory == null) {
                            Text(stringResource(R.string.report_none), color = Color.Gray)
                        } else {
                            if (viewModel.topExpenseCategory != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${stringResource(R.string.report_spent_most)}:", modifier = Modifier.weight(1f))
                                    Text(viewModel.topExpenseCategory ?: stringResource(R.string.report_none))
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            if (viewModel.topIncomeCategory != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${stringResource(R.string.report_earned_most)}:", modifier = Modifier.weight(1f))
                                    Text(viewModel.topIncomeCategory ?: stringResource(R.string.report_none))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            // Resumen de Totales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.report_total_expenses), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("${"%,.0f".format(viewModel.totalExpenses)}", fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.report_total_income), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("${"%,.0f".format(viewModel.totalIncome)}", fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            // Categorías Principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.report_spent_most), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(6.dp))
                    Text(viewModel.topExpenseCategory ?: stringResource(R.string.report_none), fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.report_earned_most),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(6.dp))
                    Text(viewModel.topIncomeCategory ?: stringResource(R.string.report_none), fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinanceReportScreenPreview() {
    // Creamos un navController falso para la previsualización
    val navController = rememberNavController()
    ReportScreen(navController = navController, userName = "Yorch")
}
