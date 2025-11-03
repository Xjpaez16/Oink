package com.example.oink.ui.navigation

sealed class NavRoutes(val route : String){
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")
}