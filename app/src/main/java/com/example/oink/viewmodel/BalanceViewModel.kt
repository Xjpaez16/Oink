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

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            isLoading = true
            try {
                // Llamamos al repositorio y actualizamos las variables de estado
                movements = repository.getAllMovements()
                totalBalance = repository.getTotalBalance()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // Solo dejamos esta función porque tiene lógica de filtrado
    fun getMovementsByType(type: MovementType): List<Movement> {
        return movements.filter { it.type == type.name }
    }
}
