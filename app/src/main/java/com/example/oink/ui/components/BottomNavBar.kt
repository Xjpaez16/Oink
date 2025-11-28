package com.example.oink.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState // IMPORTANTE
import com.example.oink.R
import com.example.oink.navigation.NavRoutes

@Composable
fun BottomNavBar(
    navController: NavController
) {
    // 1. Observamos la entrada actual de navegación
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // 2. Obtenemos la ruta actual (string)
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF2997FD)
    ) {

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.home_smile_svgrepo_com_3),
                contentDescription = stringResource(R.string.nav_home),
                tint = Color(0xFF2997FD),
                modifier = Modifier.size(24.dp)
            ) },
            // 3. Comparamos la ruta actual con la ruta de este botón
            selected = currentRoute == NavRoutes.Home.route,
            onClick = {
                // Evita recargar la pantalla si ya estás en ella
                if (currentRoute != NavRoutes.Home.route) {
                    navController.navigate(NavRoutes.Home.route) {
                        // Vuelve al inicio de la pila para no acumular pantallas
                        popUpTo(NavRoutes.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            label = { Text(stringResource(R.string.nav_home)) }
        )

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.reports_svgrepo_com__1__4),
                contentDescription = stringResource(R.string.nav_reports),
                tint = Color(0xFF2997FD),
                modifier = Modifier.size(24.dp)
            ) },
            selected = currentRoute == NavRoutes.Report.route,
            onClick = {
                if (currentRoute != NavRoutes.Report.route) {
                    navController.navigate(NavRoutes.Report.route) {
                        popUpTo(NavRoutes.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text(stringResource(R.string.nav_reports)) },
        )

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.trophy_prize_medal_svgrepo_com_3),
                contentDescription = stringResource(R.string.nav_goals),
                tint = Color(0xFF2997FD),
                modifier = Modifier.size(24.dp)
            ) },
            selected = currentRoute == NavRoutes.SelectGoal.route,
            onClick = {
                if (currentRoute != NavRoutes.SelectGoal.route) {
                    navController.navigate(NavRoutes.SelectGoal.route) {
                        popUpTo(NavRoutes.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text(stringResource(R.string.nav_goals)) }
        )

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.today),
                contentDescription = stringResource(R.string.nav_calendar),
                tint = Color(0xFF2997FD),
                modifier = Modifier.size(24.dp)
            ) },

            selected = currentRoute == NavRoutes.Consult_movs.route,
            onClick = {
                if (currentRoute != NavRoutes.Goal.route) {
                    navController.navigate(NavRoutes.Consult_movs.route) {
                        popUpTo(NavRoutes.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text(stringResource(R.string.nav_calendar)) }
        )
    }
}
