package com.example.oink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.data.model.Subcategory
import com.example.oink.viewmodel.ExpenseIncomeViewModel

@Composable
fun ExpenseChart(
    viewModel: ExpenseIncomeViewModel = viewModel(),
    scrollOffset: Float = 0f
) {
    val movements = viewModel.movements
    val hasData = movements.isNotEmpty()
    val emptySubcategory = Subcategory(name = "", icon = 0)

    // Factor para reducir solo la ALTURA de las barras
    // Se reduce gradualmente hasta desaparecer cuando scrollOffset >= 800
    val heightFactor = (1f - (scrollOffset / 800f)).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp), // Altura fija del contenedor
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .height(260.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            val bars = if (hasData) movements else List(4) {
                Movement(0, 0.0, "", "", emptySubcategory, MovementType.NONE)
            }

            val maxAmount = (movements.maxOfOrNull { it.amount } ?: 1.0)

            bars.forEachIndexed { index, movement ->
                // Calculamos la altura base de la barra
                val baseHeight = if (hasData) {
                    (movement.amount / maxAmount * 180).coerceAtLeast(50.0)
                } else listOf(180.0, 120.0, 75.0)[index % 3]

                // Aplicamos el factor de reducción SOLO a la altura
                val barHeight = baseHeight * heightFactor

                Box(
                    modifier = Modifier
                        .width(55.dp) // Ancho FIJO, no se reduce
                        .height(barHeight.dp) // Solo la altura se reduce
                        .background(
                            color = if (hasData) Color(0xFF2997FD) else Color.Black.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Solo mostramos el contenido si la barra tiene suficiente altura
                    if (hasData && heightFactor > 0.3f) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = movement.category.icon),
                                contentDescription = movement.category.name,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = formatShortAmount(movement.amount),
                                color = Color.White,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Mensaje cuando no hay datos, se desvanece con el scroll
        if (!hasData && heightFactor > 0.1f) {
            Text(
                text = "Nuevo mes, añade un movimiento",
                color = Color.Gray.copy(alpha = heightFactor),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun formatShortAmount(amount: Double): String {
    return when {
        amount >= 1_000_000 -> "${(amount / 1_000_000).format(1)}M"
        amount >= 1_000 -> "${(amount / 1_000).format(0)}k"
        else -> amount.toInt().toString()
    }
}

fun Double.format(decimals: Int): String = "%.${decimals}f".format(this)