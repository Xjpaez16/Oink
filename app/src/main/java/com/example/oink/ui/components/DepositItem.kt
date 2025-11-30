package com.example.oink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oink.R
import com.example.oink.data.model.GoalDeposit
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DepositItem(
    deposit: GoalDeposit,
    onDelete: (GoalDeposit) -> Unit
) {
    val colorAccent = Color(0xFF2997FD)
    val colorPrimary = Color(0xFF0D3685)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono de abono
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(colorAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bank_bitcoin_svgrepo_com),
                    contentDescription = "Abono",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Abono a Meta",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = dateFormat.format(deposit.date),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPrimary
                )
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Monto del abono
            Box(
                modifier = Modifier
                    .background(
                        color = colorAccent,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "+$${"%,d".format(deposit.amount)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            // Botón de eliminar
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar Abono",
                tint = Color.Red.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDelete(deposit) }
            )
        }
    }
}