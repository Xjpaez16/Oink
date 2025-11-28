package com.example.oink.data.model

import java.util.Date

data class Goal(
    val id: String = "",
    val name: String = "",
    val amountSaved: Long = 0,
    val amountTarget: Long = 0,
    val createdAt: Date = Date(),
    val userId: String = ""
)
