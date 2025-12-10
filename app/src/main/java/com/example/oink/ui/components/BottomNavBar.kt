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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.oink.R
import com.example.oink.navigation.NavRoutes

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF2997FD)
    ) {
        // Función auxiliar para navegar suavemente
        fun navigateTo(route: String) {
            if (currentRoute != route) {
                navController.navigate(route) {
                    // Volver al inicio del grafo al cambiar de pestaña
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Evitar copias múltiples de la misma pantalla
                    launchSingleTop = true
                    // Restaurar estado al volver
                    restoreState = true
                }
            }
        }

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.home_smile_svgrepo_com_3),
                contentDescription = stringResource(R.string.nav_home),
                tint = if (currentRoute == NavRoutes.Home.route) Color(0xFF2997FD) else Color.Gray,
                modifier = Modifier.size(24.dp)
            ) },
            selected = currentRoute == NavRoutes.Home.route,
            onClick = {
                if (currentRoute != NavRoutes.Home.route) {
                    navController.navigate(NavRoutes.Home.route) {

                        popUpTo(navController.graph.findStartDestination().id) {

                            saveState = false
                        }

                        launchSingleTop = true

                        restoreState = false
                    }
                }
            },
            label = {
                Text(
                    stringResource(R.string.nav_home),
                    color = if (currentRoute == NavRoutes.Home.route) Color(0xFF2997FD) else Color.Gray
                )
            }
        )

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.reports_svgrepo_com__1__4),
                contentDescription = stringResource(R.string.nav_reports),
                tint = if (currentRoute == NavRoutes.Report.route) Color(0xFF2997FD) else Color.Gray,
                modifier = Modifier.size(24.dp)
            ) },
            selected = currentRoute == NavRoutes.Report.route,
            onClick = { navigateTo(NavRoutes.Report.route) },
            label = {
                Text(
                    stringResource(R.string.nav_reports),
                    color = if (currentRoute == NavRoutes.Report.route) Color(0xFF2997FD) else Color.Gray
                )
            },
        )

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.trophy_prize_medal_svgrepo_com_3),
                contentDescription = stringResource(R.string.nav_goals),
                tint = if (currentRoute == NavRoutes.SelectGoal.route) Color(0xFF2997FD) else Color.Gray,
                modifier = Modifier.size(24.dp)
            ) },
            selected = currentRoute == NavRoutes.SelectGoal.route,
            onClick = { navigateTo(NavRoutes.SelectGoal.route) },
            label = {
                Text(
                    stringResource(R.string.nav_goals),
                    color = if (currentRoute == NavRoutes.SelectGoal.route) Color(0xFF2997FD) else Color.Gray
                )
            }
        )

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.today),
                contentDescription = stringResource(R.string.nav_calendar),
                tint = if (currentRoute == NavRoutes.Consult_movs.route) Color(0xFF2997FD) else Color.Gray,
                modifier = Modifier.size(24.dp)
            ) },
            selected = currentRoute == NavRoutes.Consult_movs.route,
            onClick = { navigateTo(NavRoutes.Consult_movs.route) },
            label = {
                Text(
                    stringResource(R.string.nav_calendar),
                    color = if (currentRoute == NavRoutes.Consult_movs.route) Color(0xFF2997FD) else Color.Gray
                )
            }
        )
    }
}
