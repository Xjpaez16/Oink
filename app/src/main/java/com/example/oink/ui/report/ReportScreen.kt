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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.oink.R
import com.example.oink.ui.components.BottomNavBar

@Composable
fun ReportScreen(
    navController: NavController, // 1. Agregamos el parámetro necesario
    userName: String
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
                text = stringResource(R.string.greeting_mr, userName),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C60E7)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.report_subtitle),
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.report_header),
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
                    Text(text = stringResource(R.string.label_from), fontSize = 12.sp, color = Color.Gray)
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
                    Text(text = stringResource(R.string.label_to), fontSize = 12.sp, color = Color.Gray)
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
                Text(stringResource(R.string.chart_placeholder_text), color = Color(0xFF1C60E7))
            }

            Spacer(Modifier.height(40.dp))

            // Resumen de Totales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.report_total_expenses), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("250.000", fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.report_total_income), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("500.000", fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            // Categorías Principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.report_spent_most), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

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
                    stringResource(R.string.report_earned_most),
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
    ReportScreen(navController = navController, userName = "Yorch")
}
