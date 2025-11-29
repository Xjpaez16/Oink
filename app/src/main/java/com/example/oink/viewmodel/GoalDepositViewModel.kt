package com.example.oink.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.repository.GoalRepository
import kotlinx.coroutines.launch

class GoalDepositViewModel : ViewModel() {

    private val repository = GoalRepository()

    val isLoading = mutableStateOf(false)
    val messageState = mutableStateOf<String?>(null)

    fun saveDeposit(userId: String, goalName: String, amount: String) {
        if (goalName.isBlank() || amount.isBlank()) {
            messageState.value = "Por favor ingresa un monto válido"
            return
        }

        if (userId.isBlank()) {
            messageState.value = "Error: Usuario no identificado."
            return
        }

        val depositAmount = amount.toLongOrNull()
        if (depositAmount == null || depositAmount <= 0) {
            messageState.value = "El monto debe ser un número positivo."
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            try {
                // Buscamos la meta por nombre y le sumamos el dinero
                repository.addDepositByName(userId, goalName, depositAmount)
                messageState.value = "¡Abono de $$depositAmount realizado!"
            } catch (e: Exception) {
                e.printStackTrace()
                messageState.value = "Error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        messageState.value = null
    }
}
