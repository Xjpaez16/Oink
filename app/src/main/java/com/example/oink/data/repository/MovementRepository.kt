package com.example.oink.data.repository

import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MovementRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val movements = db.collection("Movements")

    // --- Funciones Básicas CRUD ---

    suspend fun createMovement(movement: Movement): String {
        val ref = movements.document()
        val data = movement.copy(id = ref.id)
        ref.set(data).await()
        return ref.id
    }

    // Alias para compatibilidad si tu ViewModel llama a 'addMovement'
    suspend fun addMovement(movement: Movement) {
        createMovement(movement)
    }

    suspend fun getMovementById(id: String): Movement? =
        movements.document(id).get().await().toObject(Movement::class.java)

    // --- Funciones solicitadas por BalanceViewModel ---

    /**
     * Obtiene TODOS los movimientos de la colección.
     * OJO: En una app real deberías filtrar por 'userId' para no traer datos de otros.
     */
    suspend fun getAllMovements(): List<Movement> {
        val snapshot = movements.get().await()
        return snapshot.toObjects(Movement::class.java)
    }

    /**
     * Filtra movimientos por tipo (INGRESO o GASTO)
     */
    suspend fun getMovementsByType(type: MovementType): List<Movement> {
        val snapshot = movements
            .whereEqualTo("type", type.name) // Buscamos por el nombre del Enum (String)
            .get()
            .await()
        return snapshot.toObjects(Movement::class.java)
    }

    /**
     * Calcula el balance total (Ingresos - Gastos).
     * Nota: Esto descarga todos los movimientos para calcularlo localmente.
     */
    suspend fun getTotalBalance(): Double {
        val allMovements = getAllMovements()

        val income = allMovements
            .filter { it.type == MovementType.INCOME.name }
            .sumOf { it.amount }

        val expense = allMovements
            .filter { it.type == MovementType.EXPENSE.name }
            .sumOf { it.amount }

        return (income - expense).toDouble()
    }

    // --- Funciones Reactivas (Flow) y Avanzadas ---

    fun getMovementsByUser(userId: String) = callbackFlow {
        val listener = movements
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snap, _ ->
                val list = snap?.toObjects(Movement::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun updateMovement(movement: Movement) {
        movements.document(movement.id).set(movement).await()
    }

    suspend fun deleteMovement(id: String) {
        movements.document(id).delete().await()
    }

    suspend fun getMovementsByDateRange(userId: String, start: Long, end: Long): List<Movement> {
        val query = movements
            .whereEqualTo("userId", userId)
            .whereGreaterThanOrEqualTo("date", start)
            .whereLessThanOrEqualTo("date", end)
            .get().await()

        return query.toObjects(Movement::class.java)
    }

    suspend fun getRecurringMovements(userId: String): List<Movement> {
        val query = movements
            .whereEqualTo("userId", userId)
            .whereEqualTo("isRecurring", true)
            .get().await()

        return query.toObjects(Movement::class.java)
    }
}
