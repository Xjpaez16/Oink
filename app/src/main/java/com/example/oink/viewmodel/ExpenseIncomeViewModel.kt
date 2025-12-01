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

    // Lista de movimientos observables por la UI
    var movements by mutableStateOf<List<Movement>>(emptyList())
        private set

    // Total calculado observable
    var totalAmount by mutableDoubleStateOf(0.0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    /**
     * Carga los movimientos filtrados por tipo (Ingreso o Gasto) desde Firebase.
     */
    fun loadMovementsByType(type: MovementType, userId: String) {
        // Si el userId viene vacío (no logueado), no hacemos nada o limpiamos
        if (userId.isBlank()) {
            movements = emptyList()
            totalAmount = 0.0
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                // USAMOS LA NUEVA FUNCIÓN DEL REPOSITORIO
                val list = repository.getMovementsByUserAndType(userId, type)

                movements = list
                totalAmount = list.sumOf { it.amount }.toDouble()

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Agrega un movimiento a la base de datos y recarga la lista.
     */
    fun addMovement(movement: Movement) {
        viewModelScope.launch {
            isLoading = true
            try {
                repository.addMovement(movement)
                // Recargamos usando el ID del movimiento que acabamos de guardar
                val typeEnum = if (movement.type == MovementType.INCOME.name) MovementType.INCOME else MovementType.EXPENSE

                // PASAMOS EL USER ID AQUÍ:
                loadMovementsByType(typeEnum, movement.userId)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Limpia la lista en memoria (útil al salir de la pantalla)
     */
    fun clear() {
        movements = emptyList()
        totalAmount = 0.0
    }

    /**
     * Obtiene el total acumulado del tipo especificado.
     * Nota: Esta función en tu versión anterior era síncrona y llamaba al repo.
     * Ahora devolvemos el valor calculado en memoria 'totalAmount'
     * (asegúrate de llamar a loadMovementsByType primero).
     */
    fun getTotalByType(type: MovementType): Double {
        // Si la lista actual coincide con el tipo solicitado, devolvemos el total ya calculado.
        // De lo contrario, devolvemos 0.0 (o podrías forzar una recarga).
        val isSameType = movements.isNotEmpty() && movements.first().type == type.name
        return if (isSameType) totalAmount else 0.0
    }
}
