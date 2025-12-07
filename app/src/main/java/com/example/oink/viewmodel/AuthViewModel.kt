package com.example.oink.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.User
import com.example.oink.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val TAG = "AuthViewModel" // Etiqueta para filtrar en Logcat

    // Estados observables
    val isLoggedIn = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    val currentUser = mutableStateOf<User?>(null)

    // ------------------------------------------------------
    // REGISTRO MANUAL
    // ------------------------------------------------------
    fun register(name: String, birthDate: String, email: String, password: String) {
        Log.d(TAG, "Iniciando registro para: $email") // LOG 1

        viewModelScope.launch {
            // 1. Validaciones
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                errorMessage.value = "Por favor llena todos los campos obligatorios."
                Log.e(TAG, "Error: Campos vacíos")
                return@launch
            }

            isLoading.value = true
            errorMessage.value = null

            try {
                Log.d(TAG, "Llamando al repositorio...") // LOG 2

                // Eliminamos el delay artificial
                // delay(1000)

                // 2. Intentar registrar en Firebase (almacenamos birthDate)
                val user = userRepository.registerManual(name, birthDate, email, password)

                Log.d(TAG, "Registro exitoso en Firestore. ID: ${user.id}") // LOG 3

                // 3. Actualizar UI
                currentUser.value = user
                isLoggedIn.value = true

            } catch (e: Exception) {
                // AQUÍ ESTÁ EL ERROR REAL
                Log.e(TAG, "CRASH en registro: ${e.message}", e) // LOG 4: Imprime el error completo

                // Traducir errores comunes de Firestore para el usuario
                errorMessage.value = when {
                    e.message?.contains("PERMISSION_DENIED") == true -> "Error de permisos en base de datos."
                    e.message?.contains("UNAVAILABLE") == true -> "Sin conexión a internet."
                    e.message?.contains("already registered") == true -> "El correo ya existe." // Mensaje de tu repo
                    else -> "Error: ${e.message}"
                }
                isLoggedIn.value = false
            } finally {
                isLoading.value = false
                Log.d(TAG, "Proceso finalizado. IsLoading = false") // LOG 5
            }
        }
    }

    // Observa los cambios del usuario en Firestore en tiempo real
    private var userObserverJob: Job? = null

    fun observeUser(userId: String) {
        if (userId.isBlank()) return

        userObserverJob?.cancel()
        userObserverJob = viewModelScope.launch {
            userRepository.listenUser(userId).collectLatest { user ->
                currentUser.value = user
            }
        }
    }

    // ... El resto de tus funciones (login, google, logout) se mantienen igual ...
    // Copia el resto del archivo anterior aquí
    // ------------------------------------------------------
    fun login(email: String, password: String) {
        viewModelScope.launch {
            // 1. Validación básica de campos vacíos
            if (email.isBlank() || password.isBlank()) {
                errorMessage.value = "Ingresa correo y contraseña."
                return@launch
            }

            // 2. LIMPIEZA DE DATOS (CRÍTICO 🚨)
            // Quitamos espacios al inicio/final y convertimos a minúsculas para estandarizar
            val cleanEmail = email.trim().lowercase()
            val cleanPassword = password.trim()

            Log.d(TAG, "Intentando Login con Email: '$cleanEmail'") // Verificamos qué se envía

            isLoading.value = true
            errorMessage.value = null

            try {
                // 3. Llamada al repositorio
                val user = userRepository.loginManual(cleanEmail, cleanPassword)

                Log.d(TAG, "Login Éxitoso! Usuario: ${user.name} (${user.id})")

                currentUser.value = user
                isLoggedIn.value = true

            } catch (e: Exception) {
                Log.e(TAG, "FALLÓ LOGIN: ${e.message}") // Ver el error exacto

                // Mensajes amigables
                errorMessage.value = when {
                    e.message?.contains("Usuario no encontrado") == true -> "El correo no está registrado (Revisa mayúsculas/espacios)."
                    e.message?.contains("Contraseña incorrecta") == true -> "La contraseña no coincide."
                    e.message?.contains("Google") == true -> "Este correo usa inicio con Google."
                    else -> "Error: ${e.message}"
                }
                isLoggedIn.value = false
            } finally {
                isLoading.value = false
            }
        }
    }


    fun handleGoogleLogin(googleId: String, name: String, email: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val user = userRepository.loginWithGoogle(googleId, name, email)
                currentUser.value = user
                isLoggedIn.value = true
            } catch (e: Exception) {
                errorMessage.value = "Error Google: ${e.message}"
                isLoggedIn.value = false
            } finally {
                isLoading.value = false
            }
        }
    }

    fun registerWithGoogle(googleId: String, name: String, email: String) {
        handleGoogleLogin(googleId, name, email)
    }

    fun getLoggedUser(): User? = currentUser.value

    fun logout() {
        // Asegura que esto ocurra en el hilo principal o actualice el estado inmediatamente
        currentUser.value = null
        isLoggedIn.value = false
        errorMessage.value = null
        isLoading.value = false // Asegúrate de apagar el loading por si acaso
    }

    // Actualizar perfil del usuario
    suspend fun updateUserProfile(user: User) {
        try {
            userRepository.updateUser(user)
            currentUser.value = user
            Log.d(TAG, "Perfil actualizado correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar perfil: ${e.message}", e)
            throw e
        }
    }
}
