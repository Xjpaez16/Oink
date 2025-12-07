package com.example.oink.ui.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import com.example.oink.data.model.User
import com.example.oink.ui.theme.robotoBoldStyle
import com.example.oink.ui.theme.robotoMediumStyle
import com.example.oink.ui.theme.robotoRegularStyle
import com.example.oink.utils.LocaleHelper
import com.example.oink.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentUser = viewModel.currentUser
    val scope = rememberCoroutineScope()

    // Estados editable
    var editName by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    // Observa el usuario en tiempo real cuando tenemos userId
    LaunchedEffect(currentUser.value?.id) {
        currentUser.value?.id?.let { id ->
            viewModel.observeUser(id)
        }
    }

    // Sincroniza los campos editable con los datos del usuario
    LaunchedEffect(currentUser.value) {
        currentUser.value?.let { user ->
            editName = user.name
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // TÍTULO
            Text(
                text = stringResource(id = com.example.oink.R.string.profile_title),
                style = robotoBoldStyle(fontSize = 32.sp, color = Color(0xFF0D3685))
            )
            Spacer(modifier = Modifier.height(24.dp))

            // MOSTRAR DATOS DEL USUARIO
            if (!isEditing) {
                // Card con datos actuales
                androidx.compose.material3.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = Color(0xFFF0F4FF)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(id = com.example.oink.R.string.profile_info_title),
                            style = robotoMediumStyle(fontSize = 16.sp, color = Color(0xFF0D3685))
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        ProfileField(label = stringResource(id = com.example.oink.R.string.label_name), value = currentUser.value?.name ?: stringResource(id = com.example.oink.R.string.loading_default))
                    }
                }

                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2997FD)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = com.example.oink.R.string.btn_edit_profile),
                        style = robotoBoldStyle(fontSize = 16.sp, color = Color.White)
                    )
                }
            } else {
                // FORMULARIO DE EDICIÓN
                Text(
                    text = stringResource(id = com.example.oink.R.string.edit_profile_title),
                    style = robotoBoldStyle(fontSize = 20.sp, color = Color(0xFF0D3685))
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = com.example.oink.R.string.label_name),
                    style = robotoMediumStyle(fontSize = 14.sp, color = Color(0xFF0D3685))
                )
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text(stringResource(id = com.example.oink.R.string.hint_enter_name)) },
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            isSaving = true
                            scope.launch {
                                try {
                                    currentUser.value?.let { user ->
                                        val updatedUser = user.copy(name = editName)
                                        viewModel.updateUserProfile(updatedUser)
                                        Toast.makeText(context, context.getString(com.example.oink.R.string.msg_register_success), Toast.LENGTH_SHORT).show()
                                        isEditing = false
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isSaving = false
                                }
                            }
                        },
                        enabled = !isSaving,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2997FD)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isSaving) stringResource(id = com.example.oink.R.string.loading_default) else stringResource(id = com.example.oink.R.string.btn_save),
                            style = robotoBoldStyle(fontSize = 14.sp, color = Color.White)
                        )
                    }

                    Button(
                        onClick = { isEditing = false },
                        enabled = !isSaving,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D3685)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(id = com.example.oink.R.string.btn_cancel),
                            style = robotoBoldStyle(fontSize = 14.sp, color = Color.White)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // BOTÓN DE IDIOMA AL FINAL (llama al helper, no fuerza recreación)
            Button(
                onClick = {
                    LocaleHelper.changeLanguage(context)
                    Toast.makeText(context, context.getString(com.example.oink.R.string.btn_change_language), Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D3685)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(id = com.example.oink.R.string.btn_change_language),
                    style = robotoBoldStyle(fontSize = 16.sp, color = Color.White)
                )
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = robotoRegularStyle(fontSize = 12.sp, color = Color(0xFF7A8A9E))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = robotoMediumStyle(fontSize = 16.sp, color = Color(0xFF0D3685))
        )
    }
}
