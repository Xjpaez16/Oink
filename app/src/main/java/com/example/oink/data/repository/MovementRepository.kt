package com.example.oink.data.repository

import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType

class MovementRepository {

    private val movements = mutableListOf<Movement>()

    fun addMovement(movement: Movement) {
        movements.add(movement)
    }

    fun getAllMovements(): List<Movement> = movements

    fun getMovementsByType(type: MovementType): List<Movement> =
        movements.filter { it.type == type }

    fun getTotalBalance(): Double {
        val income = movements.filter { it.type == MovementType.INCOME }.sumOf { it.amount }
        val expense = movements.filter { it.type == MovementType.EXPENSE }.sumOf { it.amount }
        return income - expense
    }
}