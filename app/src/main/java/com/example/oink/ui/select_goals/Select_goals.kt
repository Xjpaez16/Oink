package com.example.oink.ui.select_goals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oink.R
import com.example.oink.ui.components.BottomNavBar
import com.example.oink.viewmodel.AuthViewModel
import com.example.oink.viewmodel.GoalViewModel

@Composable
fun select_goals_view(
    navController: NavController,
    authViewModel: AuthViewModel, // 1. Inyectamos AuthViewModel
    // goalViewModel: GoalViewModel = viewModel(), // Descomentar cuando implementes loadGoals()
    userName: String = "Yorch" ,
    onNavigateToadd: () -> Unit,
) {
    // val currentUser = authViewModel.currentUser.value
    // LaunchedEffect(Unit) {
    //    currentUser?.id?.let { goalViewModel.loadGoals(it) }
    // }
    // val goals = goalViewModel.goalsList // Suponiendo que tengas esta lista

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToadd,
                containerColor = Color(0xFF2997FD),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.desc_add_goal))
            }
        },
        containerColor = Color(0xFFF8FAFF)
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.greeting_mr, userName),
                    color = Color(0xFF2997FD),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 48.sp
                )
                Text(
                    text = stringResource(R.string.goals_subtitle),
                    color = Color(0xFF2997FD),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(40.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.goals_title),
                    color = Color(0xFF2997FD),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Start
                )
            }

            // AQUÍ LISTARÍAS TUS METAS REALES
            // items(goals) { goal -> ObjetivoCard(goal.name, goal.amountSaved.toDouble(), goal.amountTarget.toDouble()) }

            item {
                ObjetivoCard("Guitarra", 800000.0, 1200000.0)
                ObjetivoCard("Moto", 1800000.0, 12000000.0)
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ObjetivoCard(
    titulo: String,
    actual: Double,
    meta: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        border = BorderStroke(1.dp, Color(0xFF2997FD)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        // TODO: Agregar onClick para navegar a GoalDepositScreen con el nombre de esta meta
        // onClick = { navController.navigate("deposit_screen/$titulo") }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2997FD)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (actual / meta).coerceIn(0.0, 1.0).toFloat())
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2997FD))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.goal_progress_format, "$${String.format("%,.0f", actual)}", "$${String.format("%,.0f", meta)}"),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
