package com.example.oink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.oink.R
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.viewmodel.ExpenseIncomeViewModel

@Composable
fun ExpenseChart(
    viewModel: ExpenseIncomeViewModel = viewModel(),
    scrollOffset: Float = 0f
) {
    val movements = viewModel.movements
    val hasData = movements.isNotEmpty()

    // Igual que tu nuevo componente
    val heightFactor = (1f - (scrollOffset / 800f)).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
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

            // ESQUELETO ANTERIOR — pero usando tu lógica actual
            val bars = if (hasData) movements else List(4) {
                Movement(
                    id = "dummy",
                    amount = 0,
                    category = "",
                    type = MovementType.NONE.name
                )
            }

            val maxAmount = (movements.maxOfOrNull { it.amount } ?: 1).toDouble()

            bars.forEachIndexed { index, movement ->

                val amountDouble = movement.amount.toDouble()

                val baseHeight = if (hasData) {
                    (amountDouble / maxAmount * 180).coerceAtLeast(50.0)
                } else listOf(180.0, 120.0, 75.0)[index % 3]

                val barHeight = baseHeight * heightFactor

                Box(
                    modifier = Modifier
                        .width(55.dp)
                        .background(
                            color = if (hasData) Color(0xFF2997FD) else Color.Black.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasData && heightFactor > 0.3f) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .padding(4.dp)
                                .height(barHeight.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = getIconForCategory(movement.category)),
                                contentDescription = movement.category,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = formatShortAmount(amountDouble),
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

fun getIconForCategory(categoryName: String): Int {
    return when (categoryName) {
        "Transporte", "Transport" -> R.drawable.directions_bus
        "Hogar", "Home" -> R.drawable.house_2
        "Personal" -> R.drawable.man_hair_beauty_salon_3
        "Regalos", "Gifts" -> R.drawable.gift_1
        "Lujos", "Luxury" -> R.drawable.group
        "Comida", "Food" -> R.drawable.fast_food_french_1
        "Membresias", "Membership" -> R.drawable.netflix_1
        "Vehiculos", "Vehicles" -> R.drawable.bike_1
        "Trabajo", "Work" -> R.drawable.work_2
        "Banco", "Bank" -> R.drawable.bank_bitcoin_svgrepo_com
        else -> R.drawable.house_2
    }
}
