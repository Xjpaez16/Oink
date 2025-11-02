package com.example.oinkui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CategoryExpense(
    val emoji: String,
    val name: String,
    val amount: Int
)

@Composable
fun ExpenseChart(categories: List<CategoryExpense>) {
    val hasData = categories.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // 🔹 Fondo blanco
        contentAlignment = Alignment.Center
    ) {
        // Contenedor de barras
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 100.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            // Si no hay datos, mostramos 4 barras grises simuladas
            val bars = if (hasData) categories else List(4) { CategoryExpense("", "", 0) }

            bars.forEachIndexed { index, category ->
                val barHeight = if (hasData) {
                    (category.amount / 1000).coerceAtMost(250)
                } else {
                    // Barras grises con alturas variadas (placeholder)
                    listOf(250, 180, 140, 100)[index % 4]
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(barHeight.dp)
                            .background(
                                color = if (hasData)
                                    Color(0xFF5B8BF7) // Azul
                                else
                                    Color.Black.copy(alpha = 0.05f), // 🔹 Gris muy suave
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                    if (hasData) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = category.emoji, fontSize = 22.sp)
                        Text(
                            text = "${category.amount / 1000}k",
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Texto centrado solo si no hay datos
        if (!hasData) {
            Text(
                text = "New month, get started",
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewExpenseChart_WithData() {
    val sampleData = listOf(
        CategoryExpense("🚌", "Transporte", 50000 + 29000),

    )
    ExpenseChart(categories = sampleData)
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewExpenseChart_Empty() {
    ExpenseChart(categories = emptyList())
}
