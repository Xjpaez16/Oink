package com.example.oink.ui.goaldeposit

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.oink.ui.components.NotificationBubble
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oink.R
import com.example.oink.ui.components.DepositItem
import com.example.oink.viewmodel.AuthViewModel
import com.example.oink.viewmodel.GoalDepositViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun GoalDepositScreen(
    userName: String,
    goalId: String,
    goalName: String,
    goalPrice: String,
    authViewModel: AuthViewModel = viewModel(),
    viewModel: GoalDepositViewModel = viewModel(),
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current
    val colorPrimary = Color(0xFF1A73E8)


    var amount by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf(Date()) }


    val message by viewModel.messageState
    val isLoading by viewModel.isLoading
    val currentUser = authViewModel.currentUser.value
    //val depositsList = viewModel.depositsList // 👈 Lista reactiva de abonos
    val depositsList by viewModel.depositsList.collectAsState()
    val notificationMessage = stringResource(R.string.notification_goal_achieved)
    var showNotification by remember { mutableStateOf(false) }
    var notifyText by remember { mutableStateOf("") }
    val goalPriceValue = goalPrice.toDoubleOrNull()?.toLong() ?: 0L
    var alreadyNotified by remember { mutableStateOf(false) }

    // Carga/observación de abonos
    LaunchedEffect(goalId) {
        viewModel.loadDeposits(goalId)
    }

    // Show notification when total deposits reach or exceed goal price
    LaunchedEffect(depositsList) {
        val totalDeposits = depositsList.sumOf { it.amount }
        if (totalDeposits >= goalPriceValue && goalPriceValue > 0 && !alreadyNotified) {
            notifyText = notificationMessage
            showNotification = true
            alreadyNotified = true
        }
    }

    LaunchedEffect(showNotification) {
        if (showNotification) {
            delay(2500)
            showNotification = false
        }
    }


    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                selectedDate = calendar.time
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(top = 50.dp),
                    text = stringResource(R.string.greeting_mr, userName),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPrimary
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = goalName,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorPrimary
                )
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(width = 2.dp, color = colorPrimary, shape = RoundedCornerShape(16.dp))
                        .padding(24.dp)
                ) {
                    // ... (Botón de cerrar) ...
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 25.dp, y = (-25).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.desc_close),
                            tint = Color.Gray
                        )
                    }

                    Column {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Campo de Monto (Se mantiene igual)
                        OutlinedTextField(
                            value = amount,
                            onValueChange = {
                                if (it.all { char -> char.isDigit() }) amount = it
                            },
                            label = { Text(stringResource(R.string.label_amount)) },
                            placeholder = { Text(stringResource(R.string.placeholder_price_formatted)) },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // 🆕 Campo de Fecha (Usando DatePicker)
                        OutlinedTextField(
                            value = dateFormat.format(selectedDate),
                            onValueChange = {},
                            label = { Text("Fecha") },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.DateRange,
                                    contentDescription = "Seleccionar fecha",
                                    tint = colorPrimary,
                                    modifier = Modifier.clickable { datePickerDialog.show() }
                                )
                            },
                            readOnly = true,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { datePickerDialog.show() }
                        )
                        Spacer(modifier = Modifier.height(24.dp))


                        // Botón de Guardar
                        Button(
                            onClick = {
                                val userId = currentUser?.id ?: ""

                                viewModel.saveDeposit(userId, goalId, amount, selectedDate)
                            },
                            enabled = !isLoading && amount.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorPrimary)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Guardar abono",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }

                        // Mensaje de respuesta
                        message?.let { msg ->
                            // ... (Mostrar mensaje) ...
                            if (msg.contains("realizado", ignoreCase = true)) {
                                LaunchedEffect(msg) {
                                    amount = ""
                                    viewModel.clearMessage()
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Historial de Abonos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (depositsList.isEmpty() && !isLoading) {
                item {
                    Text(
                        text = "Aún no hay abonos registrados para esta meta.",
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Sobrecarga de `items` que acepta una 'key'.
            // `deposit.id` - clave única de Firebase.
            items(
                items = depositsList,
                key = { deposit -> deposit.id }
            ) { deposit ->
                DepositItem(
                    deposit = deposit,
                    onDelete = viewModel::deleteDeposit
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        NotificationBubble(visible = showNotification, message = notifyText)
    }
}