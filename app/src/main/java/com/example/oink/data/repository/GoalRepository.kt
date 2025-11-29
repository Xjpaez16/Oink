package com.example.oink.data.repository

import com.example.oink.data.model.Goal
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class GoalRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val collection = db.collection("goals")

    // --- Crear una nueva meta ---
    suspend fun addGoal(goal: Goal) {
        val ref = collection.document()
        // Asignamos el ID generado por Firestore al objeto antes de guardarlo
        val goalWithId = goal.copy(id = ref.id)
        ref.set(goalWithId).await()
    }

    // --- Obtener metas de un usuario específico ---
    suspend fun getGoalsByUser(userId: String): List<Goal> {
        val snapshot = collection
            .whereEqualTo("userId", userId)
            .get()
            .await()
        return snapshot.toObjects(Goal::class.java)
    }

    // --- Abonar dinero a una meta buscando por Nombre y Usuario ---
    suspend fun addDepositByName(userId: String, goalName: String, amount: Long) {
        // 1. Buscar la meta por nombre y usuario
        val query = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("name", goalName)
            .limit(1)
            .get()
            .await()

        if (!query.isEmpty) {
            val doc = query.documents.first()
            // 2. Actualizar esa meta encontrada usando incremento atómico
            doc.reference.update("amountSaved", FieldValue.increment(amount)).await()
        } else {
            throw Exception("Meta no encontrada: $goalName")
        }
    }
}
