package com.example.oink.data.model


import java.util.Date

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val authProvider: String = "", //  manual
    val passwordHash: String = "",
    val birthDate: String = "",
    val createdAt: Date = Date()
)