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

    suspend fun createMovement(movement: Movement): String {
        val ref = movements.document()
        val data = movement.copy(id = ref.id)
        ref.set(data).await()
        return ref.id
    }

    suspend fun getMovementById(id: String): Movement? =
        movements.document(id).get().await().toObject(Movement::class.java)

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
