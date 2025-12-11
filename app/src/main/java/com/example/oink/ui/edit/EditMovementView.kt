package com.example.oink.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oink.data.model.MovementType
import com.example.oink.viewmodel.ExpenseMovemetViewModel
import com.example.oink.ui.theme.robotoMediumStyle
import com.example.oink.ui.components.getIconForCategoryItem
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.rememberDatePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMovementView(
    movementId: String,
    movementViewModel: ExpenseMovemetViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    // 1. Obtenemos el movimiento. 
    // Asegúrate de haber corregido el ViewModel como vimos antes (usando Flow)
    val movementState by movementViewModel.getMovementById(movementId).collectAsState(initial = null)

    // Variables de estado del formulario
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    // Guardamos el nombre de la categoría y su icono
    var categoriaSeleccionada by remember { mutableStateOf<Pair<String, Int>?>(null) }

    var isRecurring by remember { mutableStateOf(false) }
    var selectedFrequency by remember { mutableStateOf("monthly") }

    // 2. Rellenar datos cuando carga el movimiento
    LaunchedEffect(movementState) {
        movementState?.let { mov ->
            description = mov.description
            amount = mov.amount.toString()

            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            selectedDate = formatter.format(mov.date)

            // Usamos tu función global para obtener el recurso del icono correcto
            val iconResId = getIconForCategoryItem(mov.category)
            categoriaSeleccionada = mov.category to iconResId

            isRecurring = mov.isRecurring
            selectedFrequency = mov.frequency ?: "monthly"
        }
    }

    // Loading
    if (movementState == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF2997FD))
        }
        return
    }

    val movement = movementState!!

    // --- LÓGICA DE COLORES (Igual que en MovementItem y Enter Views) ---
    val isIncome = movement.type == MovementType.INCOME.name

    // Color del texto del monto: Teal para Ingreso, Magenta para Gasto
    val amountTextColor = if (isIncome) Color(0xFF27B3AC) else Color(0xFFC6148B)

    // Color principal de botones y títulos (Azul standard)
    val mainColor = Color(0xFF2997FD)

    val titleText = if (isIncome) "Editar Ingreso" else "Editar Gasto"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Título
            Text(
                text = titleText,
                color = mainColor,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 30.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Descripción
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                textStyle = robotoMediumStyle(fontSize = 24.sp, color = Color.Black),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Monto (Con el color específico según tipo)
            TextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Monto") },
                textStyle = robotoMediumStyle(
                    fontSize = 24.sp,
                    color = amountTextColor // <--- AQUI SE APLICA EL COLOR
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Fecha
            CalendarPickerEdit(selectedDate) { selectedDate = it }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Categorías
            ListaCategoriasEdit(
                selectedCategoria = categoriaSeleccionada,
                onCategoriaSelected = { categoriaSeleccionada = it },
                isIncome = isIncome
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Switch Recurrente
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isIncome) "Ingreso Recurrente" else "Gasto Recurrente",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = isRecurring,
                    onCheckedChange = { isRecurring = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = mainColor,
                        checkedTrackColor = mainColor.copy(alpha = 0.5f)
                    )
                )
            }

            if (isRecurring) {
                Spacer(modifier = Modifier.height(8.dp))
                FrequencyDropdownEdit(
                    selectedFrequency = selectedFrequency,
                    onFrequencySelected = { selectedFrequency = it }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Botones
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 30.dp)
            ) {
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar", color = Color.White)
                }

                Button(
                    onClick = {
                        if (description.isNotEmpty() && amount.isNotEmpty() && categoriaSeleccionada != null) {

                            val dateParsed = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(selectedDate) ?: Date()
                            val amountLong = amount.toLongOrNull() ?: 0L

                            // Actualizamos el objeto
                            val updatedMovement = movement.copy(
                                description = description,
                                amount = amountLong,
                                date = dateParsed,
                                category = categoriaSeleccionada!!.first,
                                isRecurring = isRecurring,
                                frequency = if (isRecurring) selectedFrequency else null
                            )

                            movementViewModel.updateMovement(updatedMovement)
                            onBackClick()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = mainColor),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar", color = Color.White)
                }
            }
        }
    }
}

// ==========================================
// COMPONENTES AUXILIARES
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPickerEdit(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(showDialog) {
        if (showDialog && selectedDate.isNotEmpty()) {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(selectedDate)
            date?.let { datePickerState.selectedDateMillis = it.time }
        }
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calendar.timeInMillis = millis
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        onDateSelected(formatter.format(calendar.time))
                    }
                    showDialog = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            },
            colors = DatePickerDefaults.colors(containerColor = Color.White)
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2997FD)),
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = if (selectedDate.isEmpty()) "Seleccionar Fecha" else selectedDate,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Composable
fun FrequencyDropdownEdit(
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

    Box(modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(displayValue, color = Color.Black)
            Icon(Icons.Default.ExpandMore, contentDescription = null, tint = Color.Black)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
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


@Composable
fun ListaCategoriasEdit(
    selectedCategoria: Pair<String, Int>?,
    onCategoriaSelected: (Pair<String, Int>) -> Unit,
    isIncome: Boolean = false
) {
    // Filtramos las categorías según el tipo de movimiento
    val categoriasNombres = if (isIncome) {
        // Para ingresos: solo Trabajo, Regalos y Banco
        listOf("Trabajo", "Regalos", "Banco")
    } else {
        // Para gastos: todas las demás
        listOf("Transporte", "Hogar", "Personal", "Lujos", "Comida", "Membresias", "Vehiculos")
    }

    // Mapeamos los nombres a pares (Nombre, ResourceID) usando tu función existente
    val categorias = categoriasNombres.map { name ->
        name to getIconForCategoryItem(name)
    }

    Column(modifier = Modifier.padding(start = 30.dp)) {
        Text(
            text = "Categoría",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(categorias) { categoria ->
                val isSelected = selectedCategoria?.first == categoria.first
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onCategoriaSelected(categoria) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(
                                color = if (isSelected) Color(0xFF2997FD) else Color(0xFFF0F0F0),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = categoria.second),
                            contentDescription = categoria.first,
                            tint = if (isSelected) Color.White else Color.Gray,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = categoria.first,
                        fontSize = 12.sp,
                        color = if (isSelected) Color(0xFF2997FD) else Color.Gray
                    )
                }
            }
        }
    }
}
