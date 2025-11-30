package com.example.oink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.RecurringMovement
import com.example.oink.data.repository.RecurringMovementRepository
import kotlinx.coroutines.launch

class ExpenseRecurringMovementViewModel(
    private val repo: RecurringMovementRepository = RecurringMovementRepository()
) : ViewModel() {

    // ------------------------------------------------------------
    // 🟢 Crear movimiento recurrente
    // ------------------------------------------------------------
    fun createRecurringMovement(item: RecurringMovement, onSuccess: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                // Aquí podrías manejar el estado isLoading si fuera necesario
                repo.createRecurringMovement(item)
                onSuccess?.invoke()
            } catch (e: Exception) {
                // Aquí podrías manejar los errores (ej. mostrar un Toast)
                println("Error creando movimiento recurrente: ${e.message}")
            }
        }
    }

    // Puedes añadir aquí otras funciones (update, delete) para movimientos recurrentes
}