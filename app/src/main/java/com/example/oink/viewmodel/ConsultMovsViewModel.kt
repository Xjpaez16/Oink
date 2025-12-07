package com.example.oink.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.Movement
import com.example.oink.data.repository.MovementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class ConsultMovsViewModel : ViewModel() {
    private val repository = MovementRepository()

    // Estado para la lista de movimientos encontrados
    private val _movements = MutableStateFlow<List<Movement>>(emptyList())
    val movements: StateFlow<List<Movement>> = _movements

    // Estado para saber si se está cargando
    val isLoading = mutableStateOf(false)

    // Estado para saber si ya se ha realizado una búsqueda
    val searchPerformed = mutableStateOf(false)


    fun loadMovementsForDate(userId: String, date: Date) {
        viewModelScope.launch {
            isLoading.value = true
            searchPerformed.value = true

            // Inicio del día
            val startOfDay = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Fin del día
            val endOfDay = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }

            try {

                val result = repository.getMovementsByDate(
                    userId,
                    startOfDay.time,
                    endOfDay.time
                )
                _movements.value = result
            } catch (e: Exception) {
                _movements.value = emptyList()
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }
}
