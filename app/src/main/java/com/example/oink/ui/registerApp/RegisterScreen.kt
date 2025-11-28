package com.example.oink.ui.registerApp


import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.oink.ui.theme.robotoBoldStyle
import com.example.oink.R
import com.example.oink.ui.components.TextFieldWithLabel
import com.example.oink.ui.components.LoadingScreen
import com.example.oink.ui.theme.robotoRegularStyle
import com.example.oink.viewmodel.AuthViewModel
import java.util.Calendar
import androidx.core.graphics.toColorInt
import com.example.oink.ui.loginApp.GoogleSignInButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel(),
    onBackClick: () -> Unit = {}

) {
    val isLoading by viewModel.isLoading
    val isLoggedIn by viewModel.isLoggedIn


    val context = LocalContext.current
    val logo = rememberImageAsset(context, "logo.png")


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var f_nacimiento by remember {mutableStateOf("")}
    var nombre by remember {mutableStateOf("")}

    if (isLoading) {
        LoadingScreen(animationRes = R.raw.pig_money, message = stringResource(R.string.msg_creating_account))
    } else if (isLoggedIn) {
        LoadingScreen(animationRes = R.raw.pig_money, message = stringResource(R.string.msg_register_success))
    }else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Logo
                logo?.let {
                    Image(
                        bitmap = it,
                        contentDescription = stringResource(R.string.desc_logo),
                        modifier = Modifier
                            .width(230.dp)
                            .height(190.dp)
                            .offset(y = (-70).dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-50).dp, x = (12).dp)

                ) {
                    TextFieldWithLabel(
                        label = stringResource(R.string.label_name),
                        value = nombre,
                        onValueChange = { nombre = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = stringResource(R.string.label_name),
                                tint = Color.Black,


                                )
                        },
                        isPassword = false
                    )

                    DatePickerTextField(
                        label = stringResource(R.string.label_birthdate),
                        value = f_nacimiento,
                        onValueChange = { f_nacimiento = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.label_birthdate),
                                tint = Color.Black,


                                )
                        },
                    )

                    TextFieldWithLabel(
                        label = stringResource(R.string.label_email),
                        value = email,
                        onValueChange = { email = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = stringResource(R.string.desc_email),
                                tint = Color.Black
                            )
                        }
                    )

                    TextFieldWithLabel(
                        label = stringResource(R.string.label_password),
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = stringResource(R.string.desc_lock),
                                tint = Color.Black,


                                )
                        },
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.register(nombre, f_nacimiento, email, password)},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2997FD)),
                            modifier = Modifier
                                .width(201.dp)
                                .height(43.dp)
                                .offset(x = (-22).dp),
                            shape = RoundedCornerShape(8.dp)

                        ) {
                            Text(
                                text = stringResource(R.string.btn_register_action),
                                style = robotoBoldStyle(
                                    fontSize = 20.sp,
                                    color = Color.White
                                ),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_right),
                                contentDescription = stringResource(R.string.desc_arrow_right),
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Button(
                            onClick = { onBackClick() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D3685)),
                            modifier = Modifier
                                .size(width = 91.dp, height = 43.dp)
                                .offset(x = (-10).dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.corner_down_left),
                                contentDescription = stringResource(R.string.desc_arrow_right),
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        GoogleSignInButton(
                            onClick = {


                            },
                            modifier = Modifier.offset(x = (-10).dp) // Ajusta el offset si es necesario para alinearlo
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(308.dp) // Mismo ancho que tus otros botones
            .height(45.dp), // Altura estándar
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        // Borde gris suave típico de Google
        border = BorderStroke(1.dp, Color(0xFF747775)),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Nota: tint = Color.Unspecified es VITAL para ver los colores del logo
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.btn_google),
                style = robotoBoldStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF1F1F1F) // Gris oscuro oficial de Google
                )
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val showDatePicker = {

        try {
            val dialog = DatePickerDialog(
                context,
                R.style.DatePickerTheme,
                { _, year, month, day ->
                    val formatted = "%02d/%02d/%04d".format(day, month + 1, year)
                    onValueChange(formatted)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            dialog.show()
        } catch (e: Exception) {
            Log.e("DatePickerTextField", "Error mostrando DatePicker", e)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leadingIcon?.invoke()
            Text(
                text = label,
                style = robotoRegularStyle(fontSize = 16.sp, color = Color.Black),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .width(308.dp)
                .height(45.dp)
                .clickable {
                    showDatePicker()
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF2997FD),
                unfocusedIndicatorColor = Color(0xFFCAC4D1),
                disabledIndicatorColor = Color(0xFFCAC4D1),
                disabledTextColor = Color.Black
            )
        )
    }
}




@Composable
private fun rememberImageAsset(context: android.content.Context, fileName: String) =
    remember(fileName) {
        runCatching {
            val inputStream = context.assets.open(fileName)
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }.getOrNull()
    }

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen()
}
