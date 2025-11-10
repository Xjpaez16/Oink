package com.example.oink.ui.select_goals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
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

@Composable
fun select_goals_view(){
    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hola sr yorch",
                    color = Color(0xFF2997FD),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Plantemos semillas para crecer",
                    color = Color(0xFF2997FD),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center

                )

            }
            Column(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Metas",
                    color = Color(0xFF2997FD),
                    fontSize = 70.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp)

                    )

                ObjetivoCard("Guitarra", 800000.0, 1200000.0)
                ObjetivoCard("Moto", 1800000.0, 12000000.0)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp)

                ) {
                    FloatingActionButton(
                        onClick = { /* acción */ },
                        containerColor = Color(0xFF2997FD)
                    ) {
                        Text("+", fontSize = 24.sp, color = Color.White)
                    }
                }
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
            .padding(vertical = 6.dp, horizontal = 20.dp),
        border = BorderStroke(1.dp, Color(0xFF2997FD)), // borde azul
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2997FD)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Barra de progreso personalizada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (actual / meta).toFloat())
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2997FD))
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$${"%,.0f".format(actual)} De $${"%,.0f".format(meta)}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}


@Preview
@Composable
fun preview(){
    select_goals_view()
}