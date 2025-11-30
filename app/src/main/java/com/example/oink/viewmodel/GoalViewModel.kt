package com.example.oink.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.Goal
import com.example.oink.data.repository.GoalRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

class GoalViewModel : ViewModel() {

    private val repository = GoalRepository()
    val goalsList = mutableStateListOf<Goal>()
    val isLoading = mutableStateOf(false)
    val messageState = mutableStateOf<String?>(null)

    // AHORA RECIBIMOS EL userId COMO PARÁMETRO

    fun loadGoals(userId: String) {
        if (userId.isBlank() || goalsList.isNotEmpty()) {
            // Se puede agregar una bandera para evitar que se lance dos veces
            // si la lista ya está escuchando. Para simplificar, solo revisamos el ID.
            if (userId.isBlank()) return
        }

        viewModelScope.launch {
            isLoading.value = true
            try {
                // Usamos la nueva función del repositorio que devuelve el Flow en tiempo real
                repository.getGoalsByUserRealtime(userId).collectLatest { list ->
                    // Esta acción se ejecuta cada vez que Firestore envía una actualización
                    goalsList.clear()
                    goalsList.addAll(list)
                    isLoading.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                messageState.value = "Error al cargar metas: ${e.message}"
                isLoading.value = false
            }
        }
    }

    fun saveGoal(userId: String, name: String, price: String) {
        // 1. Validaciones básicas
        if (name.isBlank() || price.isBlank()) {
            messageState.value = "Por favor completa todos los campos"
            return
        }

        if (userId.isBlank()) {
            messageState.value = "Error: Usuario no identificado."
            return
        }

        // Convertir precio a Long de forma segura
        val targetAmount = price.toLongOrNull()
        if (targetAmount == null || targetAmount <= 0) {
            messageState.value = "Ingresa un precio válido."
            return
        }

        // 2. Lanzar la operación asíncrona
        viewModelScope.launch {
            isLoading.value = true
            try {
                val newGoal = Goal(
                    name = name,
                    amountSaved = 0,
                    amountTarget = targetAmount,
                    createdAt = Date(),
                    userId = userId // Usamos el ID que nos pasaron
                )
                repository.addGoal(newGoal)
                messageState.value = "¡Meta guardada con éxito!"
            } catch (e: Exception) {
                e.printStackTrace()
                messageState.value = "Error al guardar: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    // Función para limpiar el mensaje después de mostrarlo
    fun clearMessage() {
        messageState.value = null
    }
}
