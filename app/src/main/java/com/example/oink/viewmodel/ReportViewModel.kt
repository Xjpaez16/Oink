package com.example.oink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ReportViewModel : ViewModel() {

    private val _startDate = MutableStateFlow(LocalDate.now().minusMonths(1))
    val startDate: StateFlow<LocalDate> = _startDate

    private val _endDate = MutableStateFlow(LocalDate.now())
    val endDate: StateFlow<LocalDate> = _endDate

    private val _totalExpenses = MutableStateFlow(250000)
    val totalExpenses: StateFlow<Int> = _totalExpenses

    private val _totalIncome = MutableStateFlow(500000)
    val totalIncome: StateFlow<Int> = _totalIncome

    private val _mostSpentCategory = MutableStateFlow("Hogar")
    val mostSpentCategory: StateFlow<String> = _mostSpentCategory

    private val _mostIncomeCategory = MutableStateFlow("Ventas")
    val mostIncomeCategory: StateFlow<String> = _mostIncomeCategory


    fun updateStartDate(newDate: LocalDate) {
        _startDate.value = newDate
        refreshReport()
    }

    fun updateEndDate(newDate: LocalDate) {
        _endDate.value = newDate
        refreshReport()
    }


    private fun refreshReport() {
        viewModelScope.launch {

            // Simulación
            _totalExpenses.value = (100000..500000).random()
            _totalIncome.value = (200000..700000).random()

            val categories = listOf("Hogar", "Comida", "Transporte", "Salud", "Ocio", "Ventas")
            _mostSpentCategory.value = categories.random()
            _mostIncomeCategory.value = categories.random()

            // --------------------------------------------------
            // Firebase
            //
            // Firebase.firestore.collection("transacciones")
            //      .whereGreaterThanOrEqualTo("fecha", startDate.value)
            //      .whereLessThanOrEqualTo("fecha", endDate.value)
            //      .get()
            //      .addOnSuccessListener { ... }
            //
            // --------------------------------------------------
        }
    }
}
