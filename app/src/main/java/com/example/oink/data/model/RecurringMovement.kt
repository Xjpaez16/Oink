package com.example.oink.data.model

import java.util.Date

data class RecurringMovement(
    val id: String = "",
    val amount: Long = 0,
    val category: String = "",
    val createdAt: Date = Date(),
    val frequency: String = "monthly",
    val nextExecution: Date = Date(),
    val type: String = "",   // "expense" | "income"
    val userId: String = ""
)
