package com.example.oink.data.repository

import com.example.oink.data.model.User

class UserRepository {

    private val users = mutableListOf<User>()
    private var loggedUser: User? = null

    fun register(user: User): Boolean {
        if (user.email.isBlank() || user.password.isBlank() || user.name.isBlank()) {
            return false // No permitir registros incompletos
        }
        if (users.any { it.email.equals(user.email, ignoreCase = true) }) {
            return false // Ya existe un usuario con ese correo
        }

        users.add(user)
        loggedUser = user
        return true
    }

    /**
     * Inicia sesión con email y password.
     * Retorna true si las credenciales son válidas.
     */
    fun login(email: String, password: String): Boolean {
        val user = users.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }
        return if (user != null) {
            loggedUser = user
            true
        } else {
            false
        }
    }

    /**
     * Devuelve el usuario actualmente logueado (si hay).
     */
    fun getLoggedUser(): User? = loggedUser

    /**
     * Verifica si hay sesión activa.
     */
    fun isLoggedIn(): Boolean = loggedUser != null

    /**
     * Cierra la sesión actual.
     */
    fun logout() {
        loggedUser = null
    }

    /**
     * Retorna la lista de usuarios registrados (opcional, solo debug).
     */
    fun getAllUsers(): List<User> = users.toList()
}
