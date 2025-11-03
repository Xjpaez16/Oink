package com.example.oink.viewmodel

import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
// import com.google.firebase.firestore.FirebaseFirestore
class GoalViewModel : ViewModel() {

    // private val db = FirebaseFirestore.getInstance()

    fun saveGoal(name: String, price: String): String {
        if (name.isBlank() || price.isBlank()) {
            return "Por favor completa todos los campos"
        }

        /*
        viewModelScope.launch {
            val goal = hashMapOf("name" to name, "price" to price)
            db.collection("goals").add(goal)
        }
        */
        return "Meta guardada: $name - $price"
    }
}
