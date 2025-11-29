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
    fun loadMovementsByType(type: MovementType) {
        viewModelScope.launch {
            isLoading = true
            try {
                // 1. Obtenemos la lista desde el repositorio (suspend function)
                val list = repository.getMovementsByType(type)
                movements = list

                // 2. Calculamos el total sumando los montos (Long -> Double)
                totalAmount = list.sumOf { it.amount }.toDouble()

            } catch (e: Exception) {
                e.printStackTrace()
                // Aquí podrías manejar un mensaje de error si quisieras
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

                // Recargamos la lista basada en el tipo del movimiento que acabamos de agregar
                // Convertimos el String del modelo al Enum para reutilizar la función
                val typeEnum = if (movement.type == MovementType.INCOME.name) MovementType.INCOME else MovementType.EXPENSE
                loadMovementsByType(typeEnum)

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
