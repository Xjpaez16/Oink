package com.example.oink.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.data.repository.MovementRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

class ReportViewModel : ViewModel() {

    private val repository = MovementRepository()

    // Job para controlar la cancelación de búsquedas anteriores si el usuario cambia rápido la fecha
    private var searchJob: Job? = null

    var totalExpenses by mutableStateOf(0.0)
        private set

    var totalIncome by mutableStateOf(0.0)
        private set

    // LISTA DE TOTALES DE GASTOS (Para la Gráfica 1)
    var categoryTotals by mutableStateOf(listOf<Movement>())
        private set

    // LISTA DE TOTALES DE INGRESOS (Para la Gráfica 2 - NUEVO)
    var incomeTotals by mutableStateOf(listOf<Movement>())
        private set

    var topExpenseCategory by mutableStateOf<String?>(null)
        private set

    var topIncomeCategory by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    // Indica si la última consulta devolvió resultados
    var hasResults by mutableStateOf(true)
        private set

    // Fechas por defecto: Desde hace 1 mes hasta hoy
    var startDate by mutableStateOf(LocalDate.now().minusMonths(1))
        private set

    var endDate by mutableStateOf(LocalDate.now())
        private set

    /**
     * Función de entrada inicial.
     * Carga usando el rango de fechas por defecto.
     */
    fun loadReportForUser(userId: String) {
        loadReportForRange(userId, startDate, endDate)
    }

    /**
     * Actualiza las variables de estado de fecha.
     * Recuerda llamar a loadReportForRange después si necesitas refrescar.
     */
    fun updateDateRange(start: LocalDate, end: LocalDate) {
        startDate = start
        endDate = end
    }

    /**
     * Carga el reporte para un rango específico de fechas (inclusivo).
     */
    fun loadReportForRange(userId: String, start: LocalDate, end: LocalDate) {
        if (userId.isBlank()) return

        // Cancelamos búsqueda anterior para evitar conflictos
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            isLoading = true
            hasResults = true // Asumimos true temporalmente

            try {
                val zone = ZoneId.systemDefault()

                // Inicio del día: 00:00:00
                val startMillis = start.atStartOfDay(zone).toInstant().toEpochMilli()

                // Fin del día: 23:59:59.999 (Inicio del día siguiente - 1ms)
                val endMillis = end.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

                // LOGS: Revisa esto en el Logcat para depurar
                Log.d("ReportViewModel", "--------------------------------------------------")
                Log.d("ReportViewModel", "Buscando movimientos para User: $userId")
                Log.d("ReportViewModel", "Rango Fecha: $start a $end")
                Log.d("ReportViewModel", "Rango Millis: $startMillis a $endMillis")

                // Llamada al repositorio
                val list = repository.getMovementsByDateRange(userId, startMillis, endMillis)

                Log.d("ReportViewModel", "Resultados encontrados en DB: ${list.size}")

                // --- Cálculos con filtro insensible a mayúsculas ---

                // 1. Filtrar Gastos e Ingresos
                val expensesList = list.filter {
                    it.type.equals(MovementType.EXPENSE.name, ignoreCase = true)
                }
                val incomeList = list.filter {
                    it.type.equals(MovementType.INCOME.name, ignoreCase = true)
                }

                // 2. Calcular Totales Numéricos
                totalExpenses = expensesList.sumOf { it.amount }.toDouble()
                totalIncome = incomeList.sumOf { it.amount }.toDouble()

                Log.d("ReportViewModel", "Total Gastos calculado: $totalExpenses")
                Log.d("ReportViewModel", "Total Ingresos calculado: $totalIncome")

                // 3. Calcular Categoría Top Gastos (Texto)
                val expensesByCat = expensesList
                    .groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }
                topExpenseCategory = expensesByCat.maxByOrNull { it.value }?.key

                // 4. Calcular Categoría Top Ingresos (Texto)
                val incomeByCat = incomeList
                    .groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }
                topIncomeCategory = incomeByCat.maxByOrNull { it.value }?.key

                // 5. PREPARAR DATOS PARA LA GRÁFICA DE GASTOS (categoryTotals)
                val groupedExpenseData = expensesList
                    .groupBy { it.category }
                    .map { (categoryName, movements) ->
                        val totalAmount = movements.sumOf { it.amount }
                        Movement(
                            id = UUID.randomUUID().toString(), // ID temporal único
                            userId = userId,
                            amount = totalAmount,
                            type = MovementType.EXPENSE.name,
                            category = categoryName
                        )
                    }
                    .sortedByDescending { it.amount } // Ordenar: los que más gastan primero
                    .take(6) // Tomar solo el Top 6

                categoryTotals = groupedExpenseData

                // 6. PREPARAR DATOS PARA LA GRÁFICA DE INGRESOS (incomeTotals) - NUEVO
                val groupedIncomeData = incomeList
                    .groupBy { it.category }
                    .map { (categoryName, movements) ->
                        val totalAmount = movements.sumOf { it.amount }
                        Movement(
                            id = UUID.randomUUID().toString(),
                            userId = userId,
                            amount = totalAmount,
                            type = MovementType.INCOME.name,
                            category = categoryName
                        )
                    }
                    .sortedByDescending { it.amount }
                    .take(6)

                incomeTotals = groupedIncomeData

                // Actualizamos estado de resultados
                hasResults = list.isNotEmpty()

            } catch (e: Exception) {
                Log.e("ReportViewModel", "Error cargando reporte", e)
                e.printStackTrace()
                hasResults = false
            } finally {
                isLoading = false
            }
        }
    }
}
