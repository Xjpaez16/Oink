package com.example.oink.ui.enter_expenses

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
import com.example.oink.viewmodel.ExpenseRecurringMovementViewModel // Asegúrate de usar el nombre correcto del VM
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import java.util.concurrent.TimeUnit


@Composable
fun Enter_expense_view(
    movementViewModel: ExpenseMovemetViewModel = viewModel(),
    recurringViewModel: ExpenseRecurringMovementViewModel = viewModel(),
    userId: String = "TEST_USER",
    onBackClick: () -> Unit = {}
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<Pair<String, Int>?>(null) }

    // ⬇️ ESTADOS NUEVOS PARA RECURRENCIA
    var isRecurring by remember { mutableStateOf(false) }
    var selectedFrequency by remember { mutableStateOf("monthly") } // Valor por defecto
    // ⬆️ ESTADOS NUEVOS

    val noneCategory = stringResource(R.string.category_none)
    val colorAccent = Color(0xFF2997FD)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Column(modifier = Modifier.padding(top = 200.dp)) {

            // Título
            Text(
                text = stringResource(R.string.title_expenses_entry),
                color = colorAccent,
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campo descripción
            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text(
                        stringResource(R.string.hint_description), // RESTAURADO EL PLACEHOLDER
                        color = Color.LightGray,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
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
                placeholder = {
                    Text(
                        stringResource(R.string.hint_amount), // RESTAURADO EL PLACEHOLDER
                        color = Color.LightGray,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
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

            // Fecha
            CalendarPickerExample(selectedDate) { selectedDate = it }

            Spacer(modifier = Modifier.height(16.dp))

            // Categorías
            ListaCategorias( // RESTAURADA LA LLAMADA A LISTACATEGORIAS
                selectedCategoria = categoriaSeleccionada,
                onCategoriaSelected = { categoriaSeleccionada = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---------------------------------------------------------
            // 🆕 SECCIÓN DE RECURRENCIA (Switch)
            // ---------------------------------------------------------

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Gasto Recurrente",
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

            // 🆕 SELECTOR DE FRECUENCIA (CONDICIONAL)
            if (isRecurring) {
                Spacer(modifier = Modifier.height(8.dp))
                FrequencyDropdown(
                    selectedFrequency = selectedFrequency,
                    onFrequencySelected = { selectedFrequency = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            //---------------------------------------------------------
            // BOTONES
            //---------------------------------------------------------
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
                        // Lógica de validación
                        if (description.isEmpty() ||
                            amount.isEmpty() ||
                            selectedDate.isEmpty() ||
                            categoriaSeleccionada == null ||
                            (isRecurring && selectedFrequency.isEmpty())
                        ) {
                            println("Campos incompletos")
                            return@Button
                        }

                        // Preparación de datos
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val parsedDate = formatter.parse(selectedDate) ?: Date()
                        val amountLong = amount.toLongOrNull() ?: 0L
                        val categoryName = categoriaSeleccionada!!.first

                        if (isRecurring) {
                            // Flujo Recurrente
                            val nextExecutionDate = calculateNextExecution(parsedDate, selectedFrequency)

                            val recurringMovement = RecurringMovement(
                                amount = amountLong,
                                category = categoryName,
                                createdAt = parsedDate,
                                frequency = selectedFrequency,
                                nextExecution = nextExecutionDate,
                                type = MovementType.EXPENSE.name.lowercase(),
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
                                    type = MovementType.EXPENSE.name,
                                    userId = userId,
                                    frequency = selectedFrequency
                                )
                                movementViewModel.createMovement(initialMovement) {
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
                            // Flujo No Recurrente
                            val movement = Movement(
                                amount = amountLong,
                                category = categoryName,
                                date = parsedDate,
                                description = description,
                                isRecurring = false,
                                type = MovementType.EXPENSE.name,
                                userId = userId,
                                frequency = null
                            )
                            // Guardar en 'movements'
                            movementViewModel.createMovement(movement) {
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

// ---------------------------------------------------------
// 🔄 FUNCIONES RESTAURADAS
// ---------------------------------------------------------

// 🆕 Composable para el selector de Frecuencia (Corregido el Icono)
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
                imageVector = Icons.Default.ExpandMore, // USO CORRECTO DEL ICONO
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

// 🆕 Función para calcular la fecha de la próxima ejecución
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

// RESTAURADA: Lista de categorías
@Composable
fun ListaCategorias(
    selectedCategoria: Pair<String, Int>?,
    onCategoriaSelected: (Pair<String, Int>) -> Unit
) {
    val categorias: List<Pair<String, Int>> = listOf(
        stringResource(R.string.cat_transport) to R.drawable.directions_bus,
        stringResource(R.string.cat_home) to R.drawable.house_2,
        stringResource(R.string.cat_personal) to R.drawable.man_hair_beauty_salon_3,
        stringResource(R.string.cat_gifts) to R.drawable.gift_1,
        stringResource(R.string.cat_luxury) to R.drawable.group,
        stringResource(R.string.cat_food) to R.drawable.fast_food_french_1,
        stringResource(R.string.cat_membership) to R.drawable.netflix_1,
        stringResource(R.string.cat_vehicles) to R.drawable.bike_1
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

// RESTAURADA: Selector de fecha (CalendarPickerExample)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPickerExample(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val colorAccent = Color(0xFF2997FD)

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
        colors = ButtonDefaults.buttonColors(containerColor = colorAccent),
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
fun PreviewEnterExpense() {
    Enter_expense_view()
}