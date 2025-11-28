package com.example.oink.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.oink.ui.components.BottomNavBar

@Composable
fun FinanceReportScreen(
    navController: NavController // 1. Agregamos el parámetro necesario
) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.White
    ) { innerPadding -> // 2. Recibimos el padding del Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 3. Aplicamos el padding para evitar que la navbar tape el contenido
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            Text(
                text = "Sr Yorch",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C60E7)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Aqui podras ver como se ha utilizado tu dinero",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Informe",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C60E7)
            )

            Spacer(Modifier.height(24.dp))

            // Fechas (Desde - Hasta)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(text = "Desde :", fontSize = 12.sp, color = Color.Gray)
                    Spacer(Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(38.dp)
                            .border(2.dp, Color(0xFF1C60E7), RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Jun 10, 2025", fontSize = 14.sp, color = Color(0xFF1C60E7))
                    }
                }

                Column {
                    Text(text = "Hasta :", fontSize = 12.sp, color = Color.Gray)
                    Spacer(Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(38.dp)
                            .border(2.dp, Color(0xFF1C60E7), RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Agos 10, 2025", fontSize = 14.sp, color = Color(0xFF1C60E7))
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // GRÁFICA (Placeholder)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFEAF2FF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Gráfica aquí", color = Color(0xFF1C60E7))
            }

            Spacer(Modifier.height(40.dp))

            // Resumen de Totales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Gastos totales :", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("250.000", fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ingresos totales :", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("500.000", fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            // Categorías Principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Gastaste más en:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(6.dp))
                    Text("🏠 Hogar", fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Generaste más ingresos en:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(6.dp))
                    Text("📈 Ventas", fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinanceReportScreenPreview() {
    // Creamos un navController falso para la previsualización
    val navController = rememberNavController()
    FinanceReportScreen(navController = navController)
}
