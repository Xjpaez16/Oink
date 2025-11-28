package com.example.oink.ui.enter_money

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oink.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Enter_money_view(
    onBackClick: () -> Unit = {}
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<Pair<String, Int>?>(null) }
    val noneCategory = stringResource(R.string.category_none)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Column(modifier = Modifier.padding(top = 200.dp)) {

            // Título
            Text(
                text = stringResource(R.string.title_income_entry),
                color = Color(0xFF2997FD),
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campos de texto
            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text(stringResource(R.string.hint_description), color = Color.LightGray, fontSize = 40.sp, fontWeight = FontWeight.Bold) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = amount,
                onValueChange = { amount = it },
                placeholder = { Text(stringResource(R.string.hint_amount), color = Color.LightGray, fontSize = 40.sp, fontWeight = FontWeight.Bold) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de fecha
            CalendarPickerExample(selectedDate) { selectedDate = it }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de categorías
            ListaCategorias(
                selectedCategoria = categoriaSeleccionada,
                onCategoriaSelected = { categoriaSeleccionada = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones Guardar y Cancelar
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(start = 30.dp)
            ) {
                Button(
                    onClick = { onBackClick()
                        // Cancelar: limpiar todos los campos
                        description = ""
                        amount = ""
                        selectedDate = ""
                        categoriaSeleccionada = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text(stringResource(R.string.btn_cancel), color = Color.White)
                }

                Button(
                    onClick = {
                        // Guardar: procesar datos (aquí puedes enviar a tu backend o base de datos)
                        println("Descripción: $description")
                        println("Monto: $amount")
                        println("Fecha: $selectedDate")
                        println("Categoría: ${categoriaSeleccionada?.first ?: noneCategory}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2997FD))
                ) {
                    Text(stringResource(R.string.btn_save), color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ListaCategorias(
    selectedCategoria: Pair<String, Int>?,
    onCategoriaSelected: (Pair<String, Int>) -> Unit
) {
    val categorias: List<Pair<String, Int>> = listOf(
        stringResource(R.string.cat_work) to R.drawable.work_2,
        stringResource(R.string.cat_gifts) to R.drawable.donations_1,
        stringResource(R.string.cat_bank) to R.drawable.bank_bitcoin_svgrepo_com,
    )

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categorias.forEach { (nombre, drawableId) ->
            Button(
                onClick = { onCategoriaSelected(nombre to drawableId) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedCategoria?.first == nombre) Color(0xFF2997FD) else Color.Transparent
                ),
                border = BorderStroke(2.dp, color = Color(0xFF2997FD)),
                modifier = Modifier
                    .defaultMinSize(minWidth = 120.dp)
                    .height(40.dp)
            ) {
                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = nombre,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = nombre,
                    color = if (selectedCategoria?.first == nombre) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }

    selectedCategoria?.let {
        Text(
            text = stringResource(R.string.category_selected_format, it.first),
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPickerExample(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        onDateSelected(formatter.format(Date(millis)))
                    }
                    showDialog = false
                }) {
                    Text(stringResource(R.string.btn_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.btn_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2997FD)),
        modifier = Modifier
            .padding(start = 30.dp)
            .height(60.dp)
    ) {
        Text(
            if (selectedDate.isEmpty()) stringResource(R.string.date_button_placeholder) else stringResource(R.string.date_button_format, selectedDate),
            fontSize = if (selectedDate.isEmpty()) 30.sp else 14.sp
        )
    }
}

@Preview
@Composable
fun preview() {
    Enter_money_view()
}
