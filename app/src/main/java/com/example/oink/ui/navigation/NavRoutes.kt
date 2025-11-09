package com.example.oink.ui.navigation

sealed class NavRoutes(val route : String){
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")
    object Login : NavRoutes("login")
    object Register : NavRoutes("register")
    object Home : NavRoutes("balance")
    object Income : NavRoutes("income")
    object Expenses : NavRoutes("expenses")

}