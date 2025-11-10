package com.example.oink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.data.model.Subcategory
import com.example.oink.data.repository.MovementRepository
import com.example.oink.R
class ExpenseIncomeViewModel(
    private val repository: MovementRepository = MovementRepository()
) : ViewModel() {


    private val _movements = mutableStateListOf<Movement>()
    val movements: List<Movement> get() = _movements

    init {
        simulateMovements()
    }


    fun loadMovementsByType(type: MovementType) {
        _movements.clear()
        _movements.addAll(repository.getMovementsByType(type))
    }

    fun addMovement(movement: Movement) {
        repository.addMovement(movement)
        loadMovementsByType(movement.type)
    }

    fun clear() {
        _movements.clear()
    }

    fun getTotalByType(type: MovementType): Double =
        repository.getMovementsByType(type).sumOf { it.amount }


    private fun simulateMovements() {
        // Ingresos
        repository.addMovement(
            Movement(
                id = 1,
                amount = 1_200_000.0,
                description = "Salario mensual",
                date = "2025-11-03",
                category = Subcategory("Trabajo", R.drawable.work_2),
                type = MovementType.INCOME
            )
        )
        repository.addMovement(
            Movement(
                id = 2,
                amount = 450_000.0,
                description = "Venta de producto",
                date = "2025-11-01",
                category = Subcategory("Ventas", R.drawable.bank_bitcoin_svgrepo_com),
                type = MovementType.INCOME
            )
        )

        // Gastos
        repository.addMovement(
            Movement(
                id = 3,
                amount = 300_000.0,
                description = "Arriendo",
                date = "2025-11-02",
                category = Subcategory("Hogar", R.drawable.house_2),
                type = MovementType.EXPENSE
            )
        )
        repository.addMovement(
            Movement(
                id = 4,
                amount = 120_000.0,
                description = "Comida",
                date = "2025-11-03",
                category = Subcategory("Comida", R.drawable.fast_food_french_1),
                type = MovementType.EXPENSE
            )
        )
        repository.addMovement(
            Movement(
                id = 5,
                amount = 75_000.0,
                description = "Membresías",
                date = "2025-11-03",
                category = Subcategory("Membresías", R.drawable.netflix_1),
                type = MovementType.EXPENSE
            )
        )
    }
}
