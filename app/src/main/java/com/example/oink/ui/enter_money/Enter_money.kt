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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oink.R
import com.example.oink.data.model.Movement
import com.example.oink.data.model.MovementType
import com.example.oink.data.model.RecurringMovement
import com.example.oink.viewmodel.ExpenseMovemetViewModel
import com.example.oink.viewmodel.ExpenseRecurringMovementViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import java.util.concurrent.TimeUnit
import kotlin.collections.List

@Composable
fun Enter_money_view(

    movementViewModel: ExpenseMovemetViewModel = viewModel(),
    recurringViewModel: ExpenseRecurringMovementViewModel = viewModel(),
    userId: String = "TEST_USER", // Simula el ID del usuario
    onBackClick: () -> Unit = {}
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<Pair<String, Int>?>(null) }


    var isRecurring by remember { mutableStateOf(false) }
    var selectedFrequency by remember { mutableStateOf("monthly") }


    val noneCategory = stringResource(R.string.category_none)
    val colorAccent = Color(0xFF2997FD)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Column(modifier = Modifier.padding(top = 200.dp)) {


            Text(
                text = stringResource(R.string.title_income_entry),
                color = colorAccent,
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))


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

            // Campo monto
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


            CalendarPickerExample(selectedDate) { selectedDate = it }

            Spacer(modifier = Modifier.height(16.dp))


            ListaCategorias(
                selectedCategoria = categoriaSeleccionada,
                onCategoriaSelected = { categoriaSeleccionada = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ingreso Recurrente",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = isRecurring,
                    onCheckedChange = { isRecurring = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorAccent,
                        checkedTrackColor = colorAccent.copy(alpha = 0.5f)
                    )
                )
            }

            //SELECTOR DE FRECUENCIA
            if (isRecurring) {
                Spacer(modifier = Modifier.height(8.dp))
                FrequencyDropdown(
                    selectedFrequency = selectedFrequency,
                    onFrequencySelected = { selectedFrequency = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones Guardar y Cancelar
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(start = 30.dp)
            ) {
                Button(
                    onClick = {
                        // Limpiar y volver
                        description = ""
                        amount = ""
                        selectedDate = ""
                        categoriaSeleccionada = null
                        isRecurring = false
                        selectedFrequency = "monthly"
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text(stringResource(R.string.btn_cancel), color = Color.White)
                }

                Button(
                    onClick = {

                        if (description.isEmpty() ||
                            amount.isEmpty() ||
                            selectedDate.isEmpty() ||
                            categoriaSeleccionada == null ||
                            (isRecurring && selectedFrequency.isEmpty())
                        ) {
                            println("Campos incompletos")
                            return@Button
                        }


                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val parsedDate = formatter.parse(selectedDate) ?: Date()
                        val amountLong = amount.toLongOrNull() ?: 0L
                        val categoryName = categoriaSeleccionada!!.first
                        val typeIncome = MovementType.INCOME.name

                        if (isRecurring) {
                            // Flujo Recurrente: Se guarda en recurring_movements
                            val nextExecutionDate = calculateNextExecution(parsedDate, selectedFrequency)

                            val recurringMovement = RecurringMovement(
                                amount = amountLong,
                                category = categoryName,
                                createdAt = parsedDate,
                                frequency = selectedFrequency,
                                nextExecution = nextExecutionDate,
                                type = typeIncome.lowercase(), // "income"
                                userId = userId
                            )

                            // 1. Guardar en 'recurring_movements'
                            recurringViewModel.createRecurringMovement(recurringMovement) {
                                // 2. Guardar la primera instancia en 'movements'
                                val initialMovement = Movement(
                                    amount = amountLong,
                                    category = categoryName,
                                    date = parsedDate,
                                    description = description,
                                    isRecurring = true,
                                    type = typeIncome, // "INCOME"
                                    userId = userId,
                                    frequency = selectedFrequency
                                )
                                movementViewModel.createMovement(initialMovement) {
                                    println("Ingreso recurrente guardado correctamente")
                                    // Limpiar UI y volver
                                    description = ""
                                    amount = ""
                                    selectedDate = ""
                                    categoriaSeleccionada = null
                                    isRecurring = false
                                    selectedFrequency = "monthly"
                                    onBackClick()
                                }
                            }
                        } else {
                            // Flujo No Recurrente: Se guarda solo en movements
                            val movement = Movement(
                                amount = amountLong,
                                category = categoryName,
                                date = parsedDate,
                                description = description,
                                isRecurring = false,
                                type = typeIncome, // "INCOME"
                                userId = userId,
                                frequency = null
                            )
                            // Guardar en 'movements'
                            movementViewModel.createMovement(movement) {
                                println("Ingreso guardado correctamente")
                                // Limpiar UI y volver
                                description = ""
                                amount = ""
                                selectedDate = ""
                                categoriaSeleccionada = null
                                onBackClick()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorAccent)
                ) {
                    Text(stringResource(R.string.btn_save), color = Color.White)
                }
            }
        }
    }
}


// FUNCIÓN DE CÁLCULO DE NEXT EXECUTION
fun calculateNextExecution(currentDate: Date, frequency: String): Date {
    val calendar = Calendar.getInstance()
    calendar.time = currentDate

    when (frequency) {
        "daily" -> calendar.add(Calendar.DAY_OF_YEAR, 1)
        "weekly" -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
        "monthly" -> calendar.add(Calendar.MONTH, 1)
        "annually" -> calendar.add(Calendar.YEAR, 1)
    }
    return calendar.time
}

// DROPDOWN DE FRECUENCIA
@Composable
fun FrequencyDropdown(
    selectedFrequency: String,
    onFrequencySelected: (String) -> Unit
) {
    val frequencies = listOf(
        "Diariamente" to "daily",
        "Semanalmente" to "weekly",
        "Mensualmente" to "monthly",
        "Anualmente" to "annually"
    )
    var expanded by remember { mutableStateOf(false) }

    val displayValue = frequencies.find { it.second == selectedFrequency }?.first ?: "Mensualmente"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
    ) {
        Text(
            text = "Frecuencia de Repetición",
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Text(displayValue, color = Color.Black)
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            frequencies.forEach { (label, value) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onFrequencySelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}

// LISTA DE CATEGORÍAS
@Composable
fun ListaCategorias(
    selectedCategoria: Pair<String, Int>?,
    onCategoriaSelected: (Pair<String, Int>) -> Unit
) {
    // Lista de Categorias
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

// SELECTOR DE FECHA
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