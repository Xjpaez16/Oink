package com.example.oink.ui.loginApp

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oink.ui.components.TextFieldWithLabel
import com.example.oink.ui.theme.robotoBoldStyle
import com.example.oink.R



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val logo = rememberImageAsset(context, "logo.png")


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

            )  {
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

                Spacer(modifier = Modifier.height(12.dp))


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
                        onClick = { /* Acción iniciar sesión */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2997FD)),
                        modifier = Modifier
                            .width(201.dp)
                            .height(43.dp)
                            .offset(x = (-22).dp),
                        shape = RoundedCornerShape(8.dp)

                    ) {
                        Text(
                            text = stringResource(R.string.btn_login_action),
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



@Composable
private fun rememberImageAsset(context: android.content.Context, fileName: String) =
    remember(fileName) {
        runCatching {
            val inputStream = context.assets.open(fileName)
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }.getOrNull()
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

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
