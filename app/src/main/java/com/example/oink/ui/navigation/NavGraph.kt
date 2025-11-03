package com.example.oink.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oink.ui.loginApp.LoginScreen
import com.example.oink.ui.registerApp.RegisterScreen
import com.example.oink.ui.startAplication.SplashScreen
import com.example.oink.ui.startAplication.StartUpScreen

@Composable
fun AppNavGraph(navController : NavHostController ){
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route

    ){
        composable(NavRoutes.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.Onboarding.route) {
            StartUpScreen(
                onRegisterClick = {
                    navController.navigate(NavRoutes.Register.route) {
                        popUpTo(NavRoutes.Onboarding.route)
                    }
                },
                onLoginClick = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Onboarding.route)
                    }
                }
            )
        }
        composable(NavRoutes.Login.route) {
            LoginScreen(
                onBackClick = {
                navController.popBackStack()
                }
            )
        }
        composable(NavRoutes.Register.route) {
            RegisterScreen (
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}