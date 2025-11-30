package com.example.oink.data.repository

import com.example.oink.data.model.Goal
import com.example.oink.data.model.GoalDeposit
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class GoalRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val goalsCollection = db.collection("goals")


    suspend fun addGoal(goal: Goal) {
        val ref = goalsCollection.document()
        val goalWithId = goal.copy(id = ref.id)
        ref.set(goalWithId).await()
    }

    fun getGoalsByUserRealtime(userId: String) = callbackFlow<List<Goal>> {
        val listener = goalsCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val goalsList = snapshot?.toObjects(Goal::class.java) ?: emptyList()
                trySend(goalsList)
            }
        awaitClose { listener.remove() }
    }




    suspend fun addDeposit(deposit: GoalDeposit) {
        // Obtenemos la referencia a la subcolección 'deposits' dentro de la meta específica.
        val depositsSubCollection = goalsCollection.document(deposit.goalId).collection("deposits")

        val depositRef = depositsSubCollection.document()
        val depositWithId = deposit.copy(id = depositRef.id)
        depositRef.set(depositWithId).await()

        val goalRef = goalsCollection.document(deposit.goalId)
        goalRef.update("amountSaved", FieldValue.increment(deposit.amount)).await()
    }

    // Lectura de la subcolección
    fun getDepositsByGoalIdRealtime(goalId: String) = callbackFlow<List<GoalDeposit>> {
        val listener = goalsCollection.document(goalId).collection("deposits")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val depositsList = snapshot?.toObjects(GoalDeposit::class.java) ?: emptyList()
                trySend(depositsList)
            }
        awaitClose { listener.remove() }
    }

    // Eliminacion de la subcolección
    suspend fun deleteDeposit(deposit: GoalDeposit) {
        val depositRef = goalsCollection.document(deposit.goalId).collection("deposits").document(deposit.id)
        val goalRef = goalsCollection.document(deposit.goalId)

        db.runTransaction { transaction ->
            transaction.update(goalRef, "amountSaved", FieldValue.increment(-deposit.amount))
            transaction.delete(depositRef)
            null
        }.await()
    }
}
