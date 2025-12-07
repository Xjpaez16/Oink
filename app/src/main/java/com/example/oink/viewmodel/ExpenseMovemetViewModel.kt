package com.example.oink.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.data.repository.MovementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    // 🟣 Filtrar ingresos (CORREGIDO)
    // ------------------------------------------------------------
    fun getIncomes(userId: String, onResult: (List<Movement>) -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                // Usamos la consulta eficiente del repositorio
                val data = repo.getMovementsByUserAndType(userId, MovementType.INCOME)
                onResult(data)
            } catch (e: Exception) {
                errorMessage.value = e.message
                onResult(emptyList())
            } finally {
                isLoading.value = false
            }
        }
    }

    // ------------------------------------------------------------
    // 🔴 Filtrar gastos (CORREGIDO)
    // ------------------------------------------------------------
    fun getExpenses(userId: String, onResult: (List<Movement>) -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                // Usamos la consulta eficiente del repositorio
                val data = repo.getMovementsByUserAndType(userId, MovementType.EXPENSE)
                onResult(data)
            } catch (e: Exception) {
                errorMessage.value = e.message
                onResult(emptyList())
            } finally {
                isLoading.value = false
            }
        }
    }

    // ------------------------------------------------------------
    // 🟡 Balance total
    // ------------------------------------------------------------
    // Nota: Esta función sigue calculando el total global de la colección.
    // Para que sea por usuario, deberías crear 'getTotalBalanceByUser(userId)' en el repo.
    fun getBalance(onResult: (Double) -> Unit) {
        viewModelScope.launch {
            try {
                onResult(repo.getTotalBalance())
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }
    // En ExpenseMovemetViewModel.kt

    // Función para obtener un movimiento por ID (usado para cargar datos al editar)
    fun getMovementById(id: String): Flow<Movement?> = flow {
        val result = repo.getMovementById(id)
        emit(result)
    }.flowOn(Dispatchers.IO)


    // Función para actualizar (UPDATE)
    fun updateMovement(movement: Movement) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateMovement(movement)
        }
    }

}
