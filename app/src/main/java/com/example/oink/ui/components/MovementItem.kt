package com.example.oink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.ui.theme.robotoBoldStyle
import com.example.oink.ui.theme.robotoMediumStyle
import com.example.oink.ui.theme.robotoRegularStyle
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MovementItem(movement: Movement) {
    // 1. CORRECCIÓN: Comparamos el String 'type' con el nombre del Enum
    val isIncome = movement.type == MovementType.INCOME.name

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isIncome) Color(0xFF2997FD) else Color(0xFF0D3685), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // 2. CORRECCIÓN: 'category' ahora es String. Usamos la función auxiliar para obtener el ícono.
                // Nota: Asegúrate de importar o copiar la función getIconForCategory de ExpenseIncomeChart.kt
                // Si no quieres copiarla, te la pongo abajo también para que este archivo funcione solo.
                Icon(
                    painter = painterResource(id = getIconForCategoryItem(movement.category)),
                    contentDescription = movement.category, // category ya es el nombre (String)
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = movement.category, // 3. CORRECCIÓN: category es directo un String
                    style = robotoRegularStyle(
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                )
                Text(
                    text = movement.description,
                    style = robotoBoldStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF0D3685)
                    )
                )

                // 4. CORRECCIÓN: Formatear la fecha (Date) a String
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                Text(
                    text = dateFormat.format(movement.date),
                    style = robotoMediumStyle(
                        fontSize = 11.sp,
                        color = Color.Black
                    )
                )
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isIncome) Color(0xFF2997FD) else Color(0xFF0D3685),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                // 5. CORRECCIÓN: amount es Long, funciona bien con el format, pero aseguramos
                Text(
                    text = "$${"%,.0f".format(movement.amount.toFloat())}",
                    style = robotoMediumStyle(
                        fontSize = 12.sp,
                        color = Color.White
                    )
                )
            }
        }
    }
}

// Función auxiliar para obtener iconos (copia local para este archivo o impórtala si la hiciste pública)
@Composable
fun getIconForCategoryItem(categoryName: String): Int {
    // Usamos com.example.oink.R para acceder a los recursos
    return when (categoryName) {
        "Transporte", "Transport" -> com.example.oink.R.drawable.directions_bus
        "Hogar", "Home" -> com.example.oink.R.drawable.house_2
        "Personal" -> com.example.oink.R.drawable.man_hair_beauty_salon_3
        "Regalos", "Gifts" -> com.example.oink.R.drawable.gift_1
        "Lujos", "Luxury" -> com.example.oink.R.drawable.group
        "Comida", "Food" -> com.example.oink.R.drawable.fast_food_french_1
        "Membresias", "Membership" -> com.example.oink.R.drawable.netflix_1
        "Vehiculos", "Vehicles" -> com.example.oink.R.drawable.bike_1
        "Trabajo", "Work" -> com.example.oink.R.drawable.work_2
        "Banco", "Bank" -> com.example.oink.R.drawable.bank_bitcoin_svgrepo_com
        else -> com.example.oink.R.drawable.house_2
    }
}
