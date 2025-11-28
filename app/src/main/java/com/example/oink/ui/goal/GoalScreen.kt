package com.example.oink.ui.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oink.ui.components.BottomNavBar
import com.example.oink.viewmodel.GoalViewModel

@Composable
fun GoalScreen(
    userName: String,
    viewModel: GoalViewModel = viewModel(),
    onClose: () -> Unit = {},
    navController: NavController
) {
    var goalName by remember { mutableStateOf("") }
    var goalPrice by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color(0xFFF8FAFF) // Color de fondo global mejorado
    ) { innerPadding ->
        // Usamos innerPadding aquí para respetar el espacio de la barra inferior
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // <-- VITAL: Evita que la navbar tape el contenido
                .padding(horizontal = 24.dp), // Padding lateral
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // Encabezado
            Text(
                text = "Hola Sr $userName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A73E8),
                modifier = Modifier.padding(top = 20.dp)
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
                    .border(
                        width = 2.dp,
                        color = Color(0xFF1A73E8),
                        shape = RoundedCornerShape(16.dp)
                    )
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
                        // Ajuste fino para que no se salga del borde visualmente
                        .offset(x = 12.dp, y = (-12).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.Gray
                    )
                }

                Column {
                    Spacer(modifier = Modifier.height(16.dp)) // Espacio para no chocar con la X

                    // Campo Nombre
                    OutlinedTextField(
                        value = goalName,
                        onValueChange = { goalName = it },
                        label = { Text("Nombre") },
                        placeholder = { Text("Guitarra") },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Precio
                    OutlinedTextField(
                        value = goalPrice,
                        onValueChange = {
                            // Solo permite números
                            if (it.all { char -> char.isDigit() }) {
                                goalPrice = it
                            }
                        },
                        label = { Text("Precio") },
                        placeholder = { Text("1200000") },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Teclado numérico
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón Guardar
                    Button(
                        onClick = {
                            val result = viewModel.saveGoal(goalName, goalPrice)
                            message = result
                            // Limpiar campos si fue exitoso (opcional, depende de lo que retorne tu VM)
                            if (result.contains("éxito", ignoreCase = true) || result.contains("guardad", ignoreCase = true)) {
                                goalName = ""
                                goalPrice = ""
                            }
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
                            color = if (message.contains("error", true)) Color.Red else Color(0xFF1A73E8),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
