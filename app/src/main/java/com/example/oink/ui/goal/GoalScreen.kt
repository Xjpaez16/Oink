package com.example.oink.ui.goal

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oink.viewmodel.GoalViewModel

@Composable
fun GoalScreen(
    userName: String,
    viewModel: GoalViewModel = viewModel(),
    onClose: () -> Unit = {}
) {
    var goalName by remember { mutableStateOf("") }
    var goalPrice by remember { mutableStateOf("") }
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
            text = "Hola Sr $userName",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A73E8),
            modifier = Modifier
                .padding(top = 50.dp)
        )

        Text(
            text = "Plantemos semillas para crecer",
            fontSize = 17.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Título principal
        Text(
            text = "Metas",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A73E8)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Contenedor del formulario
        Box(
            modifier = Modifier
                .border(width = 2.dp, color = Color(0xFF1A73E8), shape = RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            // Botón de cerrar
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-20).dp) // opcional, para "sacarlo" un poquito más
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.Gray
                )
            }

            Column {
                Spacer(modifier = Modifier.height(8.dp))

                // Campo Nombre
                OutlinedTextField(
                    value = goalName,
                    onValueChange = { goalName = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("Guitarra") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Precio
                OutlinedTextField(
                    value = goalPrice,
                    onValueChange = { goalPrice = it },
                    label = { Text("Precio") },
                    placeholder = { Text("1.200.000") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Guardar
                Button(
                    onClick = {
                        val result = viewModel.saveGoal(goalName, goalPrice)
                        message = result
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8))
                ) {
                    Text("Guardar", color = Color.White)
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
fun GoalScreenPreview() {
    GoalScreen(userName = "Yorch")
}
