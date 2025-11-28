package com.example.oink.data.model

import androidx.annotation.DrawableRes
import java.util.Date

data class Movement(
    val id: String = "",
    val amount: Long = 0,
    val category: String = "",
    val date: Date = Date(),
    val description: String = "",
    val isRecurring: Boolean = false,
    val type: String = "",
    val userId: String = ""
)

enum class MovementType{
    INCOME,
    EXPENSE,
    NONE
}
data class Subcategory(
    val name : String,
    @DrawableRes val icon : Int
)

