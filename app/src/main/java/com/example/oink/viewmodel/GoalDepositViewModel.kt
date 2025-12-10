package com.example.oink.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.GoalDeposit
import com.example.oink.data.repository.GoalRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import kotlinx.coroutines.flow.asStateFlow

class GoalDepositViewModel : ViewModel() {

    private val repository = GoalRepository()

    val isLoading = mutableStateOf(false)
    val messageState = mutableStateOf<String?>(null)

    private val _depositsList = MutableStateFlow<List<GoalDeposit>>(emptyList())
    val depositsList: StateFlow<List<GoalDeposit>> = _depositsList.asStateFlow()


    private var depositsCollector: Job? = null

    fun loadDeposits(goalId: String) {
        if (goalId.isBlank()) return


        depositsCollector?.cancel()

        depositsCollector = viewModelScope.launch {
            try {

                repository.getDepositsByGoalIdRealtime(goalId).collectLatest { list ->
                    _depositsList.value = list
                }
            } catch (e: Exception) {
                e.printStackTrace()
                messageState.value = "Error al cargar abonos: ${e.message}"
            }
        }
    }

    fun saveDeposit(userId: String, goalId: String, amount: String, userDate: Date) {
        if (amount.isBlank() || goalId.isBlank()) {
            messageState.value = "Por favor ingresa un monto válido"
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
                val deposit = GoalDeposit(
                    userId = userId,
                    goalId = goalId,
                    amount = depositAmount,
                    date = userDate
                )
                repository.addDeposit(deposit)
                messageState.value = "¡Abono de $${depositAmount} realizado!"
            } catch (e: Exception) {
                e.printStackTrace()
                messageState.value = "Error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }


    fun deleteDeposit(deposit: GoalDeposit) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                // Deja que solo el repositorio trabaje
                repository.deleteDeposit(deposit)

                messageState.value = "Abono eliminado con éxito."
            } catch (e: Exception) {
                // ...
            } finally {
                isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        messageState.value = null
    }
}