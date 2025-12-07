package com.example.oink.ui.goal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.oink.ui.components.NotificationBubble
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oink.R
import com.example.oink.ui.components.BottomNavBar
import com.example.oink.ui.components.LoadingScreen
import com.example.oink.viewmodel.AuthViewModel
import com.example.oink.viewmodel.GoalViewModel

@Composable
fun GoalScreen(
    userName: String,
    authViewModel: AuthViewModel, // 1. Inyectamos AuthViewModel
    viewModel: GoalViewModel = viewModel(),
    onClose: () -> Unit = {},
    navController: NavController
) {
    var goalName by remember { mutableStateOf("") }
    var goalPrice by remember { mutableStateOf("") }
    var showNotification by remember { mutableStateOf(false) }
    var notifyText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // 2. Observamos los estados del ViewModel
    val message by viewModel.messageState
    val isLoading by viewModel.isLoading
    val currentUser = authViewModel.currentUser.value // Obtenemos el usuario

    val notificationMessage = stringResource(R.string.notification_goal_created)

    LaunchedEffect(message) {
        message?.let { msg ->
            if (msg.contains("éxito", ignoreCase = true)) {
                notifyText = notificationMessage
                showNotification = true
            }
        }
    }

    LaunchedEffect(showNotification) {
        if (showNotification) {
            delay(2500)
            showNotification = false
        }
    }

    // Si está cargando, mostramos loader
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF1A73E8))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = { BottomNavBar(navController) },
            containerColor = Color(0xFFF8FAFF)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.greeting_mr, userName),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A73E8),
                modifier = Modifier.padding(top = 20.dp)
            )

            Text(
                text = stringResource(R.string.goals_subtitle),
                fontSize = 17.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.goals_title),
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A73E8)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color(0xFF1A73E8),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 12.dp, y = (-12).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.desc_close),
                        tint = Color.Gray
                    )
                }

                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = goalName,
                        onValueChange = { goalName = it },
                        label = { Text(stringResource(R.string.label_goal_name)) },
                        placeholder = { Text(stringResource(R.string.placeholder_guitar)) },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = goalPrice,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() }) {
                                goalPrice = it
                            }
                        },
                        label = { Text(stringResource(R.string.label_goal_price)) },
                        placeholder = { Text(stringResource(R.string.placeholder_price_large)) },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            // 3. Llamada corregida pasando el ID del usuario
                            val userId = currentUser?.id ?: ""
                            viewModel.saveGoal(userId, goalName, goalPrice)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8))
                    ) {
                        Text(stringResource(R.string.btn_save), color = Color.White)
                    }

                    // 4. Mostrar el mensaje del estado
                    message?.let { msg ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = msg,
                            color = if (msg.contains("Error", true)) Color.Red else Color(0xFF1A73E8),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Limpiar campos si fue exitoso
                        if (msg.contains("éxito", ignoreCase = true)) {
                            goalName = ""
                            goalPrice = ""
                            // Opcional: llamar a onClose() después de un delay
                        }
                    }
                }
            }
        }
        }

        NotificationBubble(visible = showNotification, message = notifyText)
    }
}
