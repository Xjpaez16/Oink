package com.example.oink.ui.expense

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oink.R
import com.example.oink.data.model.MovementType
import com.example.oink.ui.components.BottomNavBar
import com.example.oink.ui.components.ExpenseChart
import com.example.oink.ui.components.MovementItem
import com.example.oink.ui.theme.robotoExtraBoldStyle
import com.example.oink.ui.theme.robotoMediumStyle
import com.example.oink.viewmodel.AuthViewModel
import com.example.oink.viewmodel.ExpenseIncomeViewModel

@Composable
fun ExpenseScreen(
    type: MovementType, // INCOME o EXPENSE
    viewModel: ExpenseIncomeViewModel = viewModel(),
    authViewModel: AuthViewModel,
    onNavigateToIncome: () -> Unit,
    onNavigateToEnter: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val logo = remember {
        runCatching {
            val inputStream = context.assets.open("logo.png")
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }.getOrNull()
    }

    val currentUser = authViewModel.currentUser.value
    val userId = currentUser?.id ?: ""

    LaunchedEffect(type, userId) {
        if (userId.isNotBlank()) {
            viewModel.loadMovementsByType(type, userId)
        }
    }

    val movements = viewModel.getMovementsForType(type)


    val t_expense = viewModel.getTotalForType(type)

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔹 SECCIÓN FIJA (Header + botones + total)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    logo?.let {
                        Image(
                            bitmap = it,
                            contentDescription = stringResource(R.string.desc_logo),
                            modifier = Modifier.size(80.dp)
                        )
                    }
                    IconButton(onClick = { navController.navigate(com.example.oink.navigation.NavRoutes.Profile.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = stringResource(R.string.desc_settings),
                            tint = Color(0xFF0D3685),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }




                // Botones Gastos / Ingresos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {},
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFF2997FD),
                            contentColor = Color.White
                        ),
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(stringResource(R.string.btn_minus_expenses))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = onNavigateToIncome,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2997FD)),
                        border = BorderStroke(2.dp, Color(0xFF2997FD)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(stringResource(R.string.btn_plus_income))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.total_expenses_label),
                        style = robotoMediumStyle(
                            fontSize = 24.sp,
                            color = Color.Black
                        ),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$${"%,.0f".format(t_expense)}",
                        style = robotoExtraBoldStyle(
                            fontSize = 55.sp,
                            color = Color(0xFF0D3685)
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                            .padding(end = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        ExpenseChart(
                            movements = movements, // <--- Pasa la lista filtrada (Gastos)
                            scrollOffset = scrollState.value.toFloat()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        movements.forEach { movement ->
                            MovementItem(movement = movement)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }



            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                FloatingActionButton(
                    onClick = onNavigateToEnter,
                    containerColor = Color(0xFF2997FD),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier
                        .size(50.dp)

                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.desc_add),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

