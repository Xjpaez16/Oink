package com.example.oink.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oink.data.model.Movement
import com.example.oink.ui.theme.robotoBoldStyle
import com.example.oink.ui.theme.robotoMediumStyle
import com.example.oink.ui.theme.robotoRegularStyle
import kotlinx.coroutines.delay

@Composable
fun MovementItem(movement: Movement) {
    val isIncome = movement.type.name == "INCOME"

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
                Icon(
                    painter = painterResource(id = movement.category.icon),
                    contentDescription = movement.category.name,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = movement.category.name,
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

                    Text(
                        text = movement.date,
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
                Text(
                    text = "$${"%,.0f".format(movement.amount)}",
                    style = robotoMediumStyle(
                        fontSize = 12.sp,
                        color = Color.White
                    )
                )
            }


        }
    }
}


