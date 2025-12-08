package com.example.oink.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.oink.data.model.User
import com.example.oink.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// CAMBIO 1: Cambiar ViewModel() por AndroidViewModel(application) para tener acceso al Contexto
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()
    private val TAG = "AuthViewModel"


    private val prefs = application.getSharedPreferences("oink_auth_prefs", Context.MODE_PRIVATE)
    private val KEY_USER_ID = "logged_user_id"
    private val KEY_LOGIN_TIMESTAMP = "login_timestamp"


    val isLoggedIn = mutableStateOf(false)
    val isLoading = mutableStateOf(true) // Iniciamos en true para verificar sesión al arrancar
    val errorMessage = mutableStateOf<String?>(null)

    val currentUser = mutableStateOf<User?>(null)


    init {
        checkSession()
    }

    private fun checkSession() {
        val savedUserId = prefs.getString(KEY_USER_ID, null)
        val lastLoginTime = prefs.getLong(KEY_LOGIN_TIMESTAMP, 0)


        val oneMonthMillis = 30L * 24 * 60 * 60 * 1000
        val isSessionExpired = (System.currentTimeMillis() - lastLoginTime) > oneMonthMillis

        if (savedUserId != null && !isSessionExpired) {
            Log.d(TAG, "Sesión encontrada para ID: $savedUserId. Restaurando...")
            viewModelScope.launch {
                try {
                    // Cargar datos frescos del usuario desde Firestore
                    userRepository.listenUser(savedUserId).collectLatest { user ->
                        if (user != null) {
                            currentUser.value = user
                            isLoggedIn.value = true
                            // Actualizar timestamp para extender la sesión otro mes
                            saveSession(user.id)
                        } else {
                            // Si el usuario existe en pref pero no en DB (fue borrado), cerrar sesión
                            logout()
                        }
                        isLoading.value = false
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error restaurando sesión: ${e.message}")
                    logout()
                    isLoading.value = false
                }
            }
        } else {
            if (isSessionExpired) Log.d(TAG, "La sesión ha expirado por inactividad.")
            isLoading.value = false // No hay sesión guardada
        }
    }

    // Función auxiliar para guardar sesión
    private fun saveSession(userId: String) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putLong(KEY_LOGIN_TIMESTAMP, System.currentTimeMillis())
            apply()
        }
    }

    fun register(name: String, birthDate: String, email: String, password: String) {
        Log.d(TAG, "Iniciando registro para: $email")

        viewModelScope.launch {
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                errorMessage.value = "Por favor llena todos los campos obligatorios."
                return@launch
            }

            isLoading.value = true
            errorMessage.value = null

            try {
                val user = userRepository.registerManual(name, birthDate, email, password)

                // Guardar sesión al registrarse
                saveSession(user.id)

                currentUser.value = user
                isLoggedIn.value = true
                Log.d(TAG, "Registro exitoso")

            } catch (e: Exception) {
                Log.e(TAG, "CRASH en registro: ${e.message}", e)
                errorMessage.value = when {
                    e.message?.contains("PERMISSION_DENIED") == true -> "Error de permisos."
                    e.message?.contains("UNAVAILABLE") == true -> "Sin conexión a internet."
                    e.message?.contains("already registered") == true -> "El correo ya existe."
                    else -> "Error: ${e.message}"
                }
                isLoggedIn.value = false
            } finally {
                isLoading.value = false
            }
        }
    }

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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                errorMessage.value = "Ingresa correo y contraseña."
                return@launch
            }

            val cleanEmail = email.trim().lowercase()
            val cleanPassword = password.trim()
            isLoading.value = true
            errorMessage.value = null

            try {
                val user = userRepository.loginManual(cleanEmail, cleanPassword)

                // Guardar sesión al hacer login
                saveSession(user.id)

                currentUser.value = user
                isLoggedIn.value = true
                Log.d(TAG, "Login Éxitoso")

            } catch (e: Exception) {
                Log.e(TAG, "FALLÓ LOGIN: ${e.message}")
                errorMessage.value = when {
                    e.message?.contains("Usuario no encontrado") == true -> "Correo no registrado."
                    e.message?.contains("Contraseña incorrecta") == true -> "Contraseña incorrecta."
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


                saveSession(user.id)

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
        // Borrar sesión de SharedPreferences
        prefs.edit().clear().apply()

        currentUser.value = null
        isLoggedIn.value = false
        errorMessage.value = null
        isLoading.value = false
        userObserverJob?.cancel()
    }

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
