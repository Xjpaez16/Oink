package com.example.oink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oink.R
import com.example.oink.ui.navigation.NavRoutes

@Composable
fun BottomNavBar(

    navController: NavController
) {
    var selected by remember { mutableStateOf("home") }

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF2997FD)
    ) {

        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.home_smile_svgrepo_com_3),
                contentDescription = "Right arrow",
                tint = Color(0xFF2997FD),
                modifier = Modifier.size(24.dp)
            ) },
            selected = selected == "home",

            onClick = {
                selected = "home"
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Home.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.reports_svgrepo_com__1__4),
                contentDescription = "Right arrow",
                tint = Color(0xFF2997FD),
                modifier = Modifier.size(24.dp)
            )  },
            selected = selected == "stats",
            onClick = { selected = "stats"; },
            label = { Text("Reportes") },
            
        )
        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.trophy_prize_medal_svgrepo_com_3),
                contentDescription = "Right arrow",
                tint = Color(0xFF2997FD),
                modifier = Modifier.size(24.dp)
            ) },
            selected = selected == "achievements",
            onClick = { selected = "achievements";  },
            label = { Text("Logros") }
        )
        NavigationBarItem(
            icon = { Icon(
                painter = painterResource(id = R.drawable.today),
                contentDescription = "Right arrow",
                tint = Color(0xFF2997FD),
                modifier = Modifier.size(24.dp)
            ) },
            selected = selected == "calendar",
            onClick = { selected = "calendar"; },
            label = { Text("Calendario") }
        )
    }
}


