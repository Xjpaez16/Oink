package com.example.oink.data.model

import androidx.annotation.DrawableRes

data class Movement(
    val id: Int = 0,
    val amount:Double,
    val description:String,
    val date : String,
    val category : Subcategory,
    val type  : MovementType,
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

