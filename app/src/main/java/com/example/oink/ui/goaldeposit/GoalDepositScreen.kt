package com.example.oink.ui.goaldeposit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oink.R
import com.example.oink.viewmodel.GoalDepositViewModel

@Composable
fun GoalDepositScreen(
    userName: String,
    goalName: String,
    viewModel: GoalDepositViewModel = viewModel(),
    onClose: () -> Unit = {}
) {
    var amount by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado
        Text(
            modifier = Modifier.padding(top = 50.dp),
            text = stringResource(R.string.greeting_mr, userName),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A73E8)
        )

        Text(
            text = stringResource(R.string.goal_deposit_subtitle),
            fontSize = 17.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Nombre de la meta
        Text(
            text = goalName,
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A73E8)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Contenedor principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(width = 2.dp, color = Color(0xFF1A73E8), shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            // Botón de cerrar
            IconButton(onClick = onClose,
                modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 25.dp, y = (-25).dp) ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.desc_close),
                    tint = Color.Gray
                )
            }

            Column {

                Spacer(modifier = Modifier.height(8.dp))

                // Campo Monto
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.label_amount)) },
                    placeholder = { Text(stringResource(R.string.placeholder_price_formatted)) },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Guardar
                Button(
                    onClick = {
                        message = viewModel.saveDeposit(goalName, amount)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8))
                ) {
                    Text(stringResource(R.string.btn_save), color = Color.White)
                }

                if (message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GoalDepositScreenPreview() {
    GoalDepositScreen(
        userName = "Yorch",
        goalName = "Guitarra"
    )
}
