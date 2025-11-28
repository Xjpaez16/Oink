package com.example.oink.data.repository

import com.example.oink.data.model.RecurringMovement
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RecurringMovementRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val recurring = db.collection("recurring_movements")

    suspend fun createRecurringMovement(item: RecurringMovement): String {
        val ref = recurring.document()
        val data = item.copy(id = ref.id)
        ref.set(data).await()
        return ref.id
    }

    suspend fun getById(id: String): RecurringMovement? =
        recurring.document(id).get().await().toObject(RecurringMovement::class.java)

    fun getByUser(userId: String) = callbackFlow {
        val listener = recurring
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snap, _ ->
                val list = snap?.toObjects(RecurringMovement::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun updateRecurringMovement(item: RecurringMovement) {
        recurring.document(item.id).set(item).await()
    }

    suspend fun deleteRecurringMovement(id: String) {
        recurring.document(id).delete().await()
    }

    suspend fun getNextExecutions(userId: String): List<RecurringMovement> {
        val now = System.currentTimeMillis()

        val query = recurring
            .whereEqualTo("userId", userId)
            .whereLessThanOrEqualTo("nextExecution", now)
            .get().await()

        return query.toObjects(RecurringMovement::class.java)
    }
}
