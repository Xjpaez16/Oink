package com.example.oink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oink.R
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import com.example.oink.ui.theme.robotoBoldStyle

@Composable
fun PieChartWithIcons(
    movements: List<Movement>
) {
    // 1. Mapeamos los movimientos a rebanadas del pastel
    val slices = movements.map { movement ->
        PieChartData.Slice(
            value = movement.amount.toFloat(), // Convertimos Long a Float para la gráfica
            // 2. CORRECCIÓN: 'movement.type' es String, lo comparamos con el nombre del Enum
            color = if (movement.type == MovementType.INCOME.name) Color(0xFF2997FD) else Color(0xFF0D3685)
        )
    }

    // (Eliminé la columna vacía que tenías aquí con <caret>)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PieChart(
            pieChartData = PieChartData(slices),
            sliceDrawer = SimpleSliceDrawer(100f), // Grosor del anillo
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // LEYENDA DEL GRÁFICO
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (20).dp) // Ajusté un poco el offset para que no quede tan lejos
        ) {
            // Item Ingresos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(end = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color(0xFF2997FD), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))

                // 3. Usamos stringResource
                Text(
                    text = stringResource(R.string.pie_income), // "Ingresos"
                    style = robotoBoldStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                )
            }

            // Item Gastos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(end = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color(0xFF0D3685), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))

                // 3. Usamos stringResource
                Text(
                    text = stringResource(R.string.pie_expenses), // "Gastos"
                    style = robotoBoldStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                )
            }
        }
    }
}
