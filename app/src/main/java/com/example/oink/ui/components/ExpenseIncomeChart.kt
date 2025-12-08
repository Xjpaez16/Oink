package com.example.oink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oink.R
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType

private fun getCategoryKey(categoryName: String): String {
    return when (categoryName.lowercase()) {
        "transporte", "transport" -> "TRANSPORT"
        "hogar", "home" -> "HOME"
        "personal" -> "PERSONAL"
        "regalos", "gifts" -> "GIFTS"
        "lujos", "luxury" -> "LUXURY"
        "comida", "food" -> "FOOD"
        "membresias", "membership" -> "MEMBERSHIP"
        "vehiculos", "vehicles" -> "VEHICLES"
        "trabajo", "work" -> "WORK"
        "banco", "bank" -> "BANK"
        else -> categoryName.uppercase()
    }
}

@Composable
fun ExpenseChart(
    movements: List<Movement>,
    scrollOffset: Float = 0f
) {

    val groupedMovements = remember(movements) {
        movements
            .groupBy { getCategoryKey(it.category) }
            .map { (key, movs) ->
                val representativeMovement = movs.first()
                Movement(
                    id = "group_$key",
                    amount = movs.sumOf { it.amount },
                    category = representativeMovement.category,
                    type = representativeMovement.type
                )
            }
    }

    val hasData = groupedMovements.isNotEmpty()
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


            val displayBars = if (hasData) {
                groupedMovements.takeLast(6)
            } else {

                List(5) {
                    Movement(id = "dummy", amount = 0, category = "", type = MovementType.NONE.name)
                }
            }


            val maxAmount = if (hasData) (groupedMovements.maxOfOrNull { it.amount } ?: 1).toDouble() else 100.0

            displayBars.forEachIndexed { index, movement ->
                val amountDouble = movement.amount.toDouble()


                val baseHeight = if (hasData) {
                    (amountDouble / maxAmount * 180).coerceAtLeast(50.0)
                } else {

                    listOf(100.0, 160.0, 80.0, 140.0, 60.0)[index % 5]
                }

                val barHeight = baseHeight * heightFactor

                // Renderizado de la barra individual
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .width(55.dp)
                            .height(barHeight.dp)
                            .background(
                                color = if (hasData) Color(0xFF2997FD) else Color(0xFFE0E0E0), // Azul si hay datos, Gris si es esqueleto
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp)
                            ),
                        contentAlignment = Alignment.TopCenter
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
                                    tint = Color.White, // Icono blanco sobre barra azul
                                    modifier = Modifier
                                        .size(18.dp)

                                )
                                Spacer(modifier = Modifier.height(4.dp))
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
        }

        // Texto superpuesto
        if (!hasData) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.new_month),
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }
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
    return when (getCategoryKey(categoryName)) {
        "TRANSPORT" -> R.drawable.directions_bus
        "HOME" -> R.drawable.house_2
        "PERSONAL" -> R.drawable.man_hair_beauty_salon_3
        "GIFTS" -> R.drawable.gift_1
        "LUXURY" -> R.drawable.group
        "FOOD" -> R.drawable.fast_food_french_1
        "MEMBERSHIP" -> R.drawable.netflix_1
        "VEHICLES" -> R.drawable.bike_1
        "WORK" -> R.drawable.work_2
        "BANK" -> R.drawable.bank_bitcoin_svgrepo_com
        else -> R.drawable.house_2
    }
}
