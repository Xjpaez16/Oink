package com.example.oink.ui.enter_expenses

import android.app.DatePickerDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import com.example.oink.ui.components.NotificationBubble
import com.example.oink.ui.theme.robotoBoldStyle
import com.example.oink.ui.theme.robotoMediumStyle
import com.example.oink.ui.theme.robotoSemiBoldStyle
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


    var isRecurring by remember { mutableStateOf(false) }
    var selectedFrequency by remember { mutableStateOf("monthly") } // Valor por defecto

    val context = LocalContext.current
    val noneCategory = stringResource(R.string.category_none)
    val colorAccent = Color(0xFF2997FD)
    val notificationMessage = stringResource(R.string.notification_expense_added)
    var showNotification by remember { mutableStateOf(false) }
    var notifyText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(showNotification) {
        if (showNotification) {
            delay(2500)
            showNotification = false
        }
    }
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 50.dp)
        ) {

            // Título
            Text(
                text = stringResource(R.string.title_expenses_entry),
                color = colorAccent,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp)

            )

            Spacer(modifier = Modifier.height(33.dp))

            // Campo descripción
            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text(
                        stringResource(R.string.hint_description),
                        style = robotoBoldStyle(
                            fontSize = 32.sp,
                            color = Color(0xFFCAC4D0)
                        ),
                    )
                },
                textStyle = robotoMediumStyle(
                    fontSize = 32.sp,
                    color = Color(0xFF000000)
                ),
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
                        stringResource(R.string.hint_amount),
                        style = robotoBoldStyle(
                            fontSize = 32.sp,
                            color = Color(0xFFCAC4D0)
                        ),
                    )
                },
                textStyle = robotoMediumStyle(
                    fontSize = 32.sp,
                    color = Color(0xFFC6148B)
                ),
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
            ListaCategorias(
                selectedCategoria = categoriaSeleccionada,
                onCategoriaSelected = { categoriaSeleccionada = it }
            )

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp),
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
                    ),
                    modifier = Modifier.padding(end = 16.dp)
                )
            }


            if (isRecurring) {
                Spacer(modifier = Modifier.height(8.dp))
                FrequencyDropdown(
                    selectedFrequency = selectedFrequency,
                    onFrequencySelected = { selectedFrequency = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


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
                                        // Show success notification (custom bubble)
                                        notifyText = notificationMessage
                                        showNotification = true
                                    // Limpiar UI
                                    description = ""
                                    amount = ""
                                    selectedDate = ""
                                    categoriaSeleccionada = null
                                    isRecurring = false
                                    selectedFrequency = "monthly"
                                    // Delay navigation so the notification is visible
                                    coroutineScope.launch {
                                        delay(2500)
                                        onBackClick()
                                    }
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
                                // Show success notification (custom bubble)
                                notifyText = notificationMessage
                                showNotification = true
                                // Limpiar UI
                                description = ""
                                amount = ""
                                selectedDate = ""
                                categoriaSeleccionada = null
                                // Delay navigation so the notification is visible
                                coroutineScope.launch {
                                    delay(2500)
                                    onBackClick()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorAccent)
                ) {
                    Text(stringResource(R.string.btn_save), color = Color.White)
                }
            }
        }
        NotificationBubble(visible = showNotification,
            message = notifyText,
            iconRes =R.drawable.logo,
            modifier = Modifier
                .align(Alignment.TopEnd)
                )
    }
}


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
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2997FD)),
            border = BorderStroke(2.dp, Color(0xFF2997FD)),
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
            containerColor = Color(0xFF2997FD),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            frequencies.forEach { (label, value) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            label,
                            style = robotoBoldStyle(
                                fontSize = 14.sp,
                                color = Color.White
                            ),
                        ) },
                    onClick = {
                        onFrequencySelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}


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
            .padding(horizontal = 34.dp),
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
    val context = LocalContext.current

    if (showDialog) {
        // Usamos Dialog nativo + ContextThemeWrapper para aplicar estilo XML
        AndroidViewBindingDialog(
            theme = R.style.DatePickerTheme,
            onDismiss = { showDialog = false }
        ) {
            DatePickerDialog(
                ContextThemeWrapper(context, R.style.DatePickerTheme),
                { _, year, month, dayOfMonth ->
                    onDateSelected(
                        "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                    )
                },
                // Fecha inicial
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
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
            if (selectedDate.isEmpty()) stringResource(R.string.date_button_placeholder)
            else stringResource(R.string.date_button_format, selectedDate),
            fontSize = if (selectedDate.isEmpty()) 30.sp else 14.sp
        )
    }
}

@Composable
fun AndroidViewBindingDialog(
    theme: Int,
    onDismiss: () -> Unit,
    show: () -> Unit
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val themedContext = ContextThemeWrapper(context, theme)
        show()
        onDispose { onDismiss() }
    }
}

@Preview
@Composable
fun PreviewEnterExpense() {
    Enter_expense_view()
}