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
    val totalIncome = movements
        .filter { it.type == MovementType.INCOME.name }
        .sumOf { it.amount.toDouble() }

    val totalExpense = movements
        .filter { it.type == MovementType.EXPENSE.name }
        .sumOf { it.amount.toDouble() }

    val slices = mutableListOf<PieChartData.Slice>()

    if (totalIncome > 0) {
        slices.add(
            PieChartData.Slice(
                value = totalIncome.toFloat(),
                color = Color(0xFF2997FD)
            )
        )
    }

    if (totalExpense > 0) {
        slices.add(
            PieChartData.Slice(
                value = totalExpense.toFloat(),
                color = Color(0xFF0D3685)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (slices.isNotEmpty()) {
            PieChart(
                pieChartData = PieChartData(slices),
                sliceDrawer = SimpleSliceDrawer(100f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                PieChart(
                    pieChartData = PieChartData(
                        listOf(
                            PieChartData.Slice(
                                value = 1f,
                                color = Color(0xFFE0E0E0)
                            )
                        )
                    ),
                    sliceDrawer = SimpleSliceDrawer(100f),
                    modifier = Modifier.matchParentSize()
                )

                Text(
                    text = stringResource(R.string.msg_piechart) ,
                    style = robotoBoldStyle(
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (20).dp)
        ) {
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

                Text(
                    text = stringResource(R.string.pie_income),
                    style = robotoBoldStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                )
            }

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

                Text(
                    text = stringResource(R.string.pie_expenses),
                    style = robotoBoldStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                )
            }
        }
    }
}

