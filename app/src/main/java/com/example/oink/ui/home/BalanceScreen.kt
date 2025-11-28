package com.example.oink.ui.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oink.R
import com.example.oink.ui.components.BottomNavBar
import com.example.oink.ui.components.PieChartWithIcons
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.oink.ui.components.MovementItem
import com.example.oink.ui.theme.robotoBoldStyle
import com.example.oink.ui.theme.robotoExtraBoldStyle
import com.example.oink.ui.theme.robotoMediumStyle
import com.example.oink.ui.theme.robotoRegularStyle
import com.example.oink.viewmodel.BalanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(
    viewModel: BalanceViewModel = viewModel(),
    userName: String,
    onNavigateToIncome: () -> Unit,
    onNavigateToExpenses: () -> Unit,
    navController: NavController
) {
    val movements = remember { viewModel.getAllMovements() }
    val balance = remember { viewModel.getTotalBalance() }
    val context = LocalContext.current


    val logo = remember {
        runCatching {
            val inputStream = context.assets.open("logo.png")
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }.getOrNull()
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
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
                Icon(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = stringResource(R.string.desc_settings),
                    tint = Color(0xFF0D3685),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.greeting_mr, userName),
                    style = robotoBoldStyle(
                        fontSize = 32.sp,
                        color = Color(0xFF0D3685)
                    ),
                )
                Text(
                    text = stringResource(R.string.balance_subtitle),
                    style = robotoRegularStyle(
                        fontSize = 12.sp,
                        color = Color(0xFF0D3685)
                    ),
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onNavigateToExpenses,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2997FD)),
                    border = BorderStroke(2.dp, Color(0xFF2997FD)),
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
                    text = stringResource(R.string.balance_label),
                    style = robotoMediumStyle(
                        fontSize = 24.sp,
                        color = Color.Black
                    ),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${"%,.0f".format(balance)}",
                    style = robotoExtraBoldStyle(
                        fontSize = 55.sp,
                        color = Color(0xFF0D3685)
                    ),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                PieChartWithIcons(movements = movements)
            }

            Spacer(modifier = Modifier.height(100.dp))


            movements.forEach { movement ->
                MovementItem(movement = movement)
                Spacer(modifier = Modifier
                    .height(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
