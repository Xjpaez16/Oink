package com.example.oink.data.model

import java.util.Date

data class GoalDeposit(
    val id: String = "",
    val userId: String = "",
    val goalId: String = "",
    val amount: Long = 0,
    val date: Date = Date(),
)