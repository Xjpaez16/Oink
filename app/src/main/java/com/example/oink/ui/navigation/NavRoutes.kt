package com.example.oink.ui.navigation

sealed class NavRoutes(val route : String){
    object Onboarding : NavRoutes("onboarding")
}