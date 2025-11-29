package com.example.oink.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.User
import com.example.oink.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val userRepository = UserRepository()

    // Estados observables para la UI
    val isLoggedIn = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    // Usuario actual en memoria tras loguearse
    val currentUser = mutableStateOf<User?>(null)

    // ------------------------------------------------------
    // REGISTRO MANUAL
    // ------------------------------------------------------
    fun register(name: String, birthDate: String, email: String, password: String) {
        viewModelScope.launch {
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                errorMessage.value = "Por favor llena todos los campos obligatorios."
                return@launch
            }

            isLoading.value = true
            errorMessage.value = null

            try {
                // Simulamos un pequeño delay visual
                delay(1000)

                // Nota: birthDate se recibe de la UI pero no se guarda en Firestore
                // porque el modelo User actual no tiene ese campo.
                val user = userRepository.registerManual(name, email, password)

                currentUser.value = user
                isLoggedIn.value = true

            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Error al registrarse"
                isLoggedIn.value = false
            } finally {
                isLoading.value = false
            }
        }
    }

    // ------------------------------------------------------
    // LOGIN MANUAL
    // ------------------------------------------------------
    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                errorMessage.value = "Ingresa correo y contraseña."
                return@launch
            }

            isLoading.value = true
            errorMessage.value = null

            try {
                delay(1000)

                val user = userRepository.loginManual(email, password)

                currentUser.value = user
                isLoggedIn.value = true

            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Error de inicio de sesión"
                isLoggedIn.value = false
            } finally {
                isLoading.value = false
            }
        }
    }

    // ------------------------------------------------------
    // GOOGLE (LOGIN Y REGISTRO)
    // ------------------------------------------------------

    // Esta función maneja tanto el LOGIN como el REGISTRO con Google.
    // El repositorio decide: si el email existe, loguea; si no, crea el usuario.
    fun handleGoogleLogin(googleId: String, name: String, email: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                // Llamamos al repositorio
                val user = userRepository.loginWithGoogle(googleId, name, email)

                currentUser.value = user
                isLoggedIn.value = true

            } catch (e: Exception) {
                errorMessage.value = "Error al conectar con Google: ${e.message}"
                isLoggedIn.value = false
            } finally {
                isLoading.value = false
            }
        }
    }

    // Alias para llamar desde la pantalla de Registro (hace lo mismo que login)
    fun registerWithGoogle(googleId: String, name: String, email: String) {
        handleGoogleLogin(googleId, name, email)
    }

    // ------------------------------------------------------
    // UTILS
    // ------------------------------------------------------
    fun getLoggedUser(): User? {
        return currentUser.value
    }

    fun logout() {
        currentUser.value = null
        isLoggedIn.value = false
        errorMessage.value = null
    }
}
