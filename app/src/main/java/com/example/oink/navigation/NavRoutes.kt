package com.example.oink.navigation

sealed class NavRoutes(val route : String){
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")
    object Login : NavRoutes("login")
    object Register : NavRoutes("register")
    object Home : NavRoutes("balance")
    object Income : NavRoutes("income")
    object Expenses : NavRoutes("expenses")
    object insert_expenses : NavRoutes("insert_expenses")
    object insert_income : NavRoutes("insert_income")
    object Consult_movs : NavRoutes("consult_movs")
    object  Report : NavRoutes("report")
    object  Goal : NavRoutes("goal")
    object SelectGoal : NavRoutes("select_goals")

    object DepositGoal : NavRoutes("deposit_goal_screen/{goalId}/{goalName}/{goalPrice}")
    object EditMovement : NavRoutes("edit_movement/{movementId}") {
        fun createRoute(id: String) = "edit_movement/$id"
    }

    object Profile : NavRoutes("profile")

}
