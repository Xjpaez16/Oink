package com.example.oink.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
@Composable
fun AppNavGraph(navController : NavHostController ){
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Onboarding.route

    ){

    }
}