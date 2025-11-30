package com.example.oink.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.data.repository.MovementRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExpenseMovemetViewModel(
    private val repo: MovementRepository = MovementRepository()
) : ViewModel() {

    val movements = mutableStateListOf<Movement>()
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    // ------------------------------------------------------------
    // 🔵 Cargar movimientos en tiempo real para un usuario
    // ------------------------------------------------------------
    fun observeMovements(userId: String) {
        viewModelScope.launch {
            repo.getMovementsByUser(userId).collectLatest { list ->
                movements.clear()
                movements.addAll(list)
            }
        }
    }

    // ------------------------------------------------------------
    // 🟢 Crear movimiento
    // ------------------------------------------------------------
    fun createMovement(movement: Movement, onSuccess: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                repo.addMovement(movement)
                onSuccess?.invoke()
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    // ------------------------------------------------------------
    // ✏️ Editar movimiento
    // ------------------------------------------------------------
    fun updateMovement(movement: Movement, onSuccess: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                repo.updateMovement(movement)
                onSuccess?.invoke()
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    // ------------------------------------------------------------
    // ❌ Eliminar movimiento
    // ------------------------------------------------------------
    fun deleteMovement(id: String) {
        viewModelScope.launch {
            try {
                repo.deleteMovement(id)
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }

    // ------------------------------------------------------------
    // 🟣 Filtrar ingresos
    // ------------------------------------------------------------
    fun getIncomes(userId: String, onResult: (List<Movement>) -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val data = repo.getMovementsByType(MovementType.INCOME)
                .filter { it.userId == userId }
            onResult(data)
            isLoading.value = false
        }
    }

    // ------------------------------------------------------------
    // 🔴 Filtrar gastos
    // ------------------------------------------------------------
    fun getExpenses(userId: String, onResult: (List<Movement>) -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val data = repo.getMovementsByType(MovementType.EXPENSE)
                .filter { it.userId == userId }
            onResult(data)
            isLoading.value = false
        }
    }

    // ------------------------------------------------------------
    // 🟡 Balance total
    // ------------------------------------------------------------
    fun getBalance(onResult: (Double) -> Unit) {
        viewModelScope.launch {
            onResult(repo.getTotalBalance())
        }
    }
}
