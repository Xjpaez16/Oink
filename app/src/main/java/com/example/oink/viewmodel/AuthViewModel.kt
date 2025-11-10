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

    /**
     * Registra un nuevo usuario (con animación de carga simulada)
     */
    fun register(name: String, birthDate: String, email: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            // Simulamos una pequeña espera (por ejemplo 1.5 segundos)
            delay(1500)

            val user = User(name, birthDate, email, password)
            delay(1500)
            val success = userRepository.register(user)

            if (success) {

                isLoggedIn.value = true
            } else {
                errorMessage.value = "El correo ya está registrado o hay campos vacíos."
            }

            isLoading.value = false
        }
    }

    /**
     * Inicia sesión con email y password (con animación de carga simulada)
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            // Simulamos un pequeño delay de carga (por ejemplo 1 segundo)
            delay(1000)

            val success = userRepository.login(email, password)

            if (success) {
                isLoggedIn.value = true
            } else {
                errorMessage.value = "Credenciales incorrectas."
            }

            isLoading.value = false
        }
    }

    /**
     * Devuelve el usuario actualmente logueado.
     */
    fun getLoggedUser(): User? = userRepository.getLoggedUser()

    /**
     * Cierra sesión.
     */
    fun logout() {
        userRepository.logout()
        isLoggedIn.value = false
    }
}
