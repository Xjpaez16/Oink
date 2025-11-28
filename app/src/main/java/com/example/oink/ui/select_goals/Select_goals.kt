package com.example.oink.ui.select_goals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.oink.ui.components.BottomNavBar

@Composable
fun select_goals_view(
    navController: NavController,
    userName: String = "Yorch" ,
    onNavigateToadd: () -> Unit,// Parámetro opcional para el nombre
) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        // Movemos el botón flotante aquí para que sea fijo y nativo
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToadd,
                containerColor = Color(0xFF2997FD),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar meta")
            }
        },
        containerColor = Color(0xFFF8FAFF)
    ) { innerPadding ->

        // Usamos LazyColumn para permitir SCROLL si hay muchas metas
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // <--- CORRECCIÓN VITAL: Aplicamos el padding del Scaffold
                .padding(horizontal = 20.dp), // Padding lateral unificado
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Item 1: Encabezado y Saludo
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "Hola Sr $userName",
                    color = Color(0xFF2997FD),
                    fontSize = 40.sp, // Ajustado para que quepa mejor
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 48.sp
                )
                Text(
                    text = "Plantemos semillas para crecer",
                    color = Color(0xFF2997FD),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(40.dp))
            }

            // Item 2: Título de sección
            item {
                Text(
                    text = "Metas",
                    color = Color(0xFF2997FD),
                    fontSize = 48.sp, // Ajustado
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Start
                )
            }

            // Item 3: Lista de tarjetas (Aquí irían tus datos dinámicos)
            item {
                ObjetivoCard("Guitarra", 800000.0, 1200000.0)
                ObjetivoCard("Moto", 1800000.0, 12000000.0)
                // Puedes agregar más para probar el scroll
                Spacer(modifier = Modifier.height(80.dp)) // Espacio final para que el FAB no tape el último item
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
            .padding(vertical = 8.dp), // Espacio entre tarjetas
        border = BorderStroke(1.dp, Color(0xFF2997FD)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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

            // Barra de progreso
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (actual / meta).coerceIn(0.0, 1.0).toFloat()) // Evita crash si supera el 100%
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2997FD))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${"%,.0f".format(actual)} de $${"%,.0f".format(meta)}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}


