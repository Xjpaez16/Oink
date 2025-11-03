package com.example.oink.data.repository

import com.example.oink.data.model.User

class UserRepository {
    private val users = mutableListOf<User>()
    private var loggedUser: User? = null

    fun register(user: User): Boolean {
        if (users.any { it.email == user.email }) {
            return false // Ya existe
        }
        users.add(user)
        loggedUser = user
        return true
    }

    fun login(email: String, password: String): Boolean {
        val user = users.find { it.email == email && it.password == password }
        loggedUser = user
        return user != null
    }

    fun getLoggedUser(): User? = loggedUser

    fun logout() {
        loggedUser = null
    }
}