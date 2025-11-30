package com.example.oink.data.repository

import com.example.oink.data.model.Goal
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose // 👈 NUEVO
import kotlinx.coroutines.flow.callbackFlow // 👈 NUEVO
import kotlinx.coroutines.tasks.await

class GoalRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val collection = db.collection("goals")

    // --- Crear una nueva meta (se mantiene igual) ---
    suspend fun addGoal(goal: Goal) {
        val ref = collection.document()
        val goalWithId = goal.copy(id = ref.id)
        ref.set(goalWithId).await()
    }

    // --- 🆕 OBTENER METAS EN TIEMPO REAL CON FLOW (CORRECCIÓN CLAVE) ---
    // Ahora devuelve un Flow<List<Goal>>, lo que permite la observación continua.
    fun getGoalsByUserRealtime(userId: String) = callbackFlow<List<Goal>> {
        val listener = collection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // Cierra el Flow si hay error
                    return@addSnapshotListener
                }
                // Mapea y emite la lista cada vez que hay un cambio en Firestore
                val goalsList = snapshot?.toObjects(Goal::class.java) ?: emptyList()
                trySend(goalsList)
            }

        // Esta lambda se llama cuando el colector del Flow cancela la escucha
        awaitClose { listener.remove() }
    }

    // La versión 'suspend fun getGoalsByUser' anterior ya no es necesaria para la UI que requiere refresco.

    // --- Abonar dinero a una meta buscando por Nombre y Usuario (se mantiene igual) ---
    suspend fun addDepositByName(userId: String, goalName: String, amount: Long) {
        // ... (Tu lógica de addDepositByName se mantiene igual) ...
        val query = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("name", goalName)
            .limit(1)
            .get()
            .await()

        if (!query.isEmpty) {
            val doc = query.documents.first()
            doc.reference.update("amountSaved", FieldValue.increment(amount)).await()
        } else {
            throw Exception("Meta no encontrada: $goalName")
        }
    }
}