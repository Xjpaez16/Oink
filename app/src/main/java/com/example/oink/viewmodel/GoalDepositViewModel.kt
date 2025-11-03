package com.example.oink.viewmodel

import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
// import com.google.firebase.firestore.FirebaseFirestore

class GoalDepositViewModel : ViewModel() {

    // private val db = FirebaseFirestore.getInstance()

    fun saveDeposit(goalName: String, amount: String): String {
        if (goalName.isBlank() || amount.isBlank()) {
            return "Por favor ingresa un monto válido"
        }
        /*
        viewModelScope.launch {
            val deposit = hashMapOf(
                "goalName" to goalName,
                "amount" to amount
            )
            db.collection("deposits").add(deposit)
        }
        */

        return "Abono registrado localmente para $goalName: $amount"
    }
}
