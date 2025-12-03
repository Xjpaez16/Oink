package com.example.oink.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.MovementType
import com.example.oink.data.repository.MovementRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class ReportViewModel : ViewModel() {

    private val repository = MovementRepository()

    var totalExpenses by mutableStateOf(0.0)
        private set

    var totalIncome by mutableStateOf(0.0)
        private set

    var topExpenseCategory by mutableStateOf<String?>(null)
        private set

    var topIncomeCategory by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var startDate by mutableStateOf(LocalDate.now().minusMonths(1))
        private set

    var endDate by mutableStateOf(LocalDate.now())
        private set

    /**
     * Starts collecting movements for the given user and updates report state.
     */
    fun loadReportForUser(userId: String) {
        if (userId.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            try {
                repository.getMovementsByUser(userId).collect { list ->
                    // Totals
                    totalExpenses = list
                        .filter { it.type == MovementType.EXPENSE.name }
                        .sumOf { it.amount }
                        .toDouble()

                    totalIncome = list
                        .filter { it.type == MovementType.INCOME.name }
                        .sumOf { it.amount }
                        .toDouble()

                    // Top categories
                    val expensesByCat = list
                        .filter { it.type == MovementType.EXPENSE.name }
                        .groupBy { it.category }
                        .mapValues { entry -> entry.value.sumOf { it.amount } }

                    topExpenseCategory = expensesByCat.maxByOrNull { it.value }?.key

                    val incomeByCat = list
                        .filter { it.type == MovementType.INCOME.name }
                        .groupBy { it.category }
                        .mapValues { entry -> entry.value.sumOf { it.amount } }

                    topIncomeCategory = incomeByCat.maxByOrNull { it.value }?.key
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun updateDateRange(start: LocalDate, end: LocalDate) {
        startDate = start
        endDate = end
    }

    /**
     * Load report for a specific date range (inclusive). Dates are LocalDate and converted to epoch millis.
     */
    fun loadReportForRange(userId: String, start: LocalDate, end: LocalDate) {
        if (userId.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            try {
                val zone = ZoneId.systemDefault()
                val startMillis = start.atStartOfDay(zone).toInstant().toEpochMilli()
                // end of day for end date
                val endMillis = end.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

                val list = repository.getMovementsByDateRange(userId, startMillis, endMillis)

                totalExpenses = list
                    .filter { it.type == MovementType.EXPENSE.name }
                    .sumOf { it.amount }
                    .toDouble()

                totalIncome = list
                    .filter { it.type == MovementType.INCOME.name }
                    .sumOf { it.amount }
                    .toDouble()

                val expensesByCat = list
                    .filter { it.type == MovementType.EXPENSE.name }
                    .groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }

                topExpenseCategory = expensesByCat.maxByOrNull { it.value }?.key

                val incomeByCat = list
                    .filter { it.type == MovementType.INCOME.name }
                    .groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }

                topIncomeCategory = incomeByCat.maxByOrNull { it.value }?.key

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}
