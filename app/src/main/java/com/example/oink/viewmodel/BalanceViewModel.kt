package com.example.oink.viewmodel

import androidx.lifecycle.ViewModel
import com.example.oink.R
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.data.model.Subcategory
import com.example.oink.data.repository.MovementRepository

class BalanceViewModel : ViewModel() {

    private val repository = MovementRepository()

    init {
        // Simulación de datos hasta que la vista de agregar esté lista
        simulateMovements()
    }

    /**
     * Devuelve todos los movimientos (ingresos + gastos)
     * para el gráfico de balance general.
     */
    fun getAllMovements(): List<Movement> = repository.getAllMovements()

    /**
     * Devuelve los movimientos filtrados por tipo.
     */
    fun getMovementsByType(type: MovementType): List<Movement> = repository.getMovementsByType(type)

    /**
     * Calcula el balance general (ingresos - gastos)
     */
    fun getTotalBalance(): Double = repository.getTotalBalance()

    /**
     * 🔹 Simula algunos movimientos de ejemplo
     */
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
