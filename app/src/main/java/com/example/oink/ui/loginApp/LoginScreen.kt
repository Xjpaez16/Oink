package com.example.oink.ui.loginApp

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
// Credenciales Google
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

import com.example.oink.R
import com.example.oink.ui.components.TextFieldWithLabel
import com.example.oink.ui.components.LoadingScreen
import com.example.oink.ui.theme.robotoBoldStyle
import com.example.oink.utils.LocaleHelper
import com.example.oink.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onToggleLanguage: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val logo = rememberImageAsset(context, "logo.png")

    // Estados del ViewModel
    val isLoading by viewModel.isLoading
    val isLoggedIn by viewModel.isLoggedIn
    val errorMessage by viewModel.errorMessage

    // Estados locales
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Mostrar errores
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    // --- LÓGICA DE GOOGLE SIGN-IN ---
    val credentialManager = CredentialManager.create(context)
    // REEMPLAZA CON TU CLIENT ID
    val webClientId = "TU_WEB_CLIENT_ID_AQUI.apps.googleusercontent.com"

    val onGoogleSignInClick = {
        scope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                    viewModel.handleGoogleLogin(
                        googleId = googleIdTokenCredential.id,
                        name = googleIdTokenCredential.displayName ?: "Usuario Google",
                        email = email
                    )
                }
            } catch (e: GetCredentialException) {
                Log.e("LoginGoogle", "Error obteniendo credencial", e)
                Toast.makeText(context, "Error Google: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("LoginGoogle", "Error general", e)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // --------------------------------

    if (isLoading) {
        LoadingScreen(animationRes = R.raw.pig_money, message = stringResource(R.string.msg_logging_in))
    } else if (isLoggedIn) {
        LoadingScreen(animationRes = R.raw.pig_money, message = stringResource(R.string.msg_welcome))
        // Aquí el AppNavGraph detectará el cambio de isLoggedIn y navegará al Home
    } else {
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

                    // Email
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

                    // Contraseña
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
                        // Botón INICIAR SESIÓN
                        Button(
                            onClick = { viewModel.login(email, password) },
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

                        // Botón ATRÁS
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
                    }

                    // Botón GOOGLE
                    Spacer(modifier = Modifier.height(16.dp))
                    GoogleSignInButton(
                        onClick = { onGoogleSignInClick() },
                        modifier = Modifier.offset(x = (-10).dp)
                    )

                    // Botón CAMBIAR IDIOMA
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            LocaleHelper.changeLanguage(context)
                            Toast.makeText(context, context.getString(R.string.btn_change_language), Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(43.dp)
                            .offset(x = (-10).dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D3685)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.btn_change_language),
                            style = robotoBoldStyle(
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        )
                    }
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
            .width(308.dp)
            .height(45.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
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
                    color = Color(0xFF1F1F1F)
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
