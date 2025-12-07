package com.example.oink.ui.report

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oink.viewmodel.ReportViewModel
import com.example.oink.viewmodel.AuthViewModel
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

// --- IMPORTS PARA TU COMPONENTE PERSONALIZADO ---
import com.example.oink.ui.components.ExpenseChart
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType

@OptIn(ExperimentalFoundationApi::class) // Necesario para el HorizontalPager
@Composable
fun ReportScreen(
    navController: NavController,
    userName: String,
    viewModel: ReportViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    // Obtener usuario actual
    val currentUser = authViewModel.getLoggedUser()
    val userId = currentUser?.id ?: ""

    // Carga inicial con el rango por defecto
    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            viewModel.loadReportForRange(userId, viewModel.startDate, viewModel.endDate)
        }
    }

    // Formateador para los campos de fecha
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Copias locales para los campos de texto
    var startText by remember { mutableStateOf(viewModel.startDate.format(formatter)) }
    var endText by remember { mutableStateOf(viewModel.endDate.format(formatter)) }

    // Sincronizar texto si el ViewModel cambia las fechas
    LaunchedEffect(viewModel.startDate, viewModel.endDate) {
        startText = viewModel.startDate.format(formatter)
        endText = viewModel.endDate.format(formatter)
    }

    // ESTADO DEL CARRUSEL (2 Páginas: Gastos e Ingresos)
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
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

            // Sección de selectores de Fecha
            Column(modifier = Modifier.fillMaxWidth()) {
                DatePickerTextField(
                    label = stringResource(R.string.label_from),
                    value = startText,
                    onValueChange = { new ->
                        try {
                            val parsed = LocalDate.parse(new, formatter)
                            viewModel.updateDateRange(parsed, viewModel.endDate)
                            viewModel.loadReportForRange(userId, parsed, viewModel.endDate)
                        } catch (e: Exception) { /* ignorar errores de parseo */ }
                    }
                )

                Spacer(Modifier.height(8.dp))

                DatePickerTextField(
                    label = stringResource(R.string.label_to),
                    value = endText,
                    onValueChange = { new ->
                        try {
                            val parsed = LocalDate.parse(new, formatter)
                            viewModel.updateDateRange(viewModel.startDate, parsed)
                            viewModel.loadReportForRange(userId, viewModel.startDate, parsed)
                        } catch (e: Exception) { /* ignorar errores de parseo */ }
                    }
                )
            }

            Spacer(Modifier.height(28.dp))

            // ============================================================
            // CARRUSEL (PAGER) DE GRÁFICAS
            // ============================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp) // Altura aumentada para incluir los puntitos
                    .background(Color(0xFFEAF2FF), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (!viewModel.hasResults) {
                    Text(
                        text = stringResource(R.string.report_no_data_range),
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Componente de Carrusel Deslizable
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) { page ->
                            // Lógica para diferenciar las páginas
                            val isExpensePage = page == 0

                            // Configuración dinámica según la página
                            val title = if (isExpensePage) "Gastos por Categoría" else "Ingresos por Categoría"
                            val chartData = if (isExpensePage) viewModel.categoryTotals else viewModel.incomeTotals
                            // Color azul normal para gastos, Azul oscuro para ingresos
                            val titleColor = if (isExpensePage) Color(0xFF1C60E7) else Color(0xFF0D47A1)

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = title,
                                    color = titleColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                if (chartData.isEmpty()) {
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (isExpensePage) "Sin gastos registrados" else "Sin ingresos registrados",
                                            color = Color.Gray
                                        )
                                    }
                                } else {
                                    // Renderizamos la gráfica con los datos correspondientes
                                    ExpenseChart(
                                        movements = chartData,
                                        scrollOffset = 0f
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Indicadores de página (Puntitos)
                        Row(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(pagerState.pageCount) { iteration ->
                                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray.copy(alpha = 0.5f)
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(8.dp) // Tamaño del puntito
                                )
                            }
                        }
                    }
                }
            }
            // ============================================================

            Spacer(Modifier.height(40.dp))

            // Resumen de Totales Numéricos
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
    val navController = rememberNavController()
    ReportScreen(navController = navController, userName = "Usuario")
}
