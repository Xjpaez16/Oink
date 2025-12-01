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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BalanceViewModel : ViewModel() {

    private val repository = MovementRepository()

    // --- ESTADOS OBSERVABLES ---
    // Al ser 'var' y publicos, se pueden leer directamente desde la UI
    var movements by mutableStateOf<List<Movement>>(emptyList())
        private set

    var totalBalance by mutableDoubleStateOf(0.0)
        private set

    var isLoading by mutableStateOf(false)
        private set



    fun loadDataForUser(userId: String) {
        if (userId.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            try {

                repository.getMovementsByUser(userId).collectLatest { userMovements ->

                    movements = userMovements


                    val income = userMovements
                        .filter { it.type == MovementType.INCOME.name }
                        .sumOf { it.amount }

                    val expense = userMovements
                        .filter { it.type == MovementType.EXPENSE.name }
                        .sumOf { it.amount }

                    totalBalance = (income - expense).toDouble()

                    // Quitamos el loading una vez recibimos el primer dato
                    isLoading = false
                }

            } catch (e: Exception) {
                e.printStackTrace()
                isLoading = false
            }
        }
    }


    // Solo dejamos esta función porque tiene lógica de filtrado
    fun getMovementsByType(type: MovementType): List<Movement> {
        return movements.filter { it.type == type.name }
    }
}
