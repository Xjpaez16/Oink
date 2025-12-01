package com.example.oink.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.data.repository.MovementRepository
import kotlinx.coroutines.launch

class ExpenseIncomeViewModel : ViewModel() {

    private val repository = MovementRepository()

    // --- CAJA DE GASTOS ---
    var expenseMovements by mutableStateOf<List<Movement>>(emptyList())
        private set
    var expenseTotal by mutableDoubleStateOf(0.0)
        private set

    // --- CAJA DE INGRESOS ---
    var incomeMovements by mutableStateOf<List<Movement>>(emptyList())
        private set
    var incomeTotal by mutableDoubleStateOf(0.0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    /**
     * Carga los movimientos y los guarda en la variable CORRECTA según el tipo.
     */
    fun loadMovementsByType(type: MovementType, userId: String) {
        if (userId.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            try {
                val list = repository.getMovementsByUserAndType(userId, type)
                val total = list.sumOf { it.amount }.toDouble()

                // AQUÍ ESTÁ EL TRUCO: Guardamos en variables separadas
                if (type == MovementType.EXPENSE) {
                    expenseMovements = list
                    expenseTotal = total
                } else {
                    incomeMovements = list
                    incomeTotal = total
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun addMovement(movement: Movement) {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.addMovement(movement)

                // Recargamos solo la caja correspondiente al tipo de movimiento guardado
                val typeEnum = if (movement.type == MovementType.INCOME.name) MovementType.INCOME else MovementType.EXPENSE
                loadMovementsByType(typeEnum, movement.userId)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // --- HELPERS PARA LA UI ---
    // Estos métodos ayudan a que la UI sepa qué lista pintar sin saber de lógica interna

    fun getMovementsForType(type: MovementType): List<Movement> {
        return if (type == MovementType.EXPENSE) expenseMovements else incomeMovements
    }

    fun getTotalForType(type: MovementType): Double {
        return if (type == MovementType.EXPENSE) expenseTotal else incomeTotal
    }
}
