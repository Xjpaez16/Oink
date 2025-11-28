package com.example.oink.data.repository

import com.example.oink.data.model.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.mindrot.jbcrypt.BCrypt
import java.util.Date

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val users = db.collection("User")

    suspend fun registerManual(
        name: String,
        email: String,
        password: String
    ): User {
        // Verificar si el email ya existe
        val existing = getUserByEmail(email)
        if (existing != null) {
            throw Exception("El correo ya está registrado.")
        }

        // Crear hash de la contraseña
        val hash = BCrypt.hashpw(password, BCrypt.gensalt())

        // Crear documento
        val ref = users.document()
        val user = User(
            id = ref.id,
            name = name,
            email = email,
            authProvider = "manual",
            passwordHash = hash,
            createdAt = Date()
        )

        ref.set(user).await()
        return user
    }



    suspend fun loginManual(email: String, password: String): User {
        val user = getUserByEmail(email)
            ?: throw Exception("Usuario no encontrado.")

        if (user.authProvider != "manual") {
            throw Exception("Este email pertenece a un usuario de Google.")
        }

        // Verificar contraseña
        val isValid = BCrypt.checkpw(password, user.passwordHash)

        if (!isValid) {
            throw Exception("Contraseña incorrecta.")
        }

        return user
    }



    suspend fun loginWithGoogle(
        googleId: String,
        name: String,
        email: String
    ): User {
        // Verificar si el usuario ya existe
        val existing = getUserByEmail(email)
        if (existing != null) {
            return existing
        }

        // Crear usuario nuevo
        val ref = users.document()
        val user = User(
            id = ref.id,
            name = name,
            email = email,
            authProvider = "google",
            passwordHash = "", // No se usa para login con Google
            createdAt = Date()
        )

        ref.set(user).await()
        return user
    }



    suspend fun createUser(user: User): String {
        users.document(user.id).set(user).await()
        return user.id
    }

    suspend fun getUserById(id: String): User? {
        val doc = users.document(id).get().await()
        return doc.toObject(User::class.java)
    }

    suspend fun getUserByEmail(email: String): User? {
        val query = users
            .whereEqualTo("email", email)
            .get()
            .await()

        return query.documents.firstOrNull()?.toObject(User::class.java)
    }

    suspend fun updateUser(user: User) {
        users.document(user.id).set(user).await()
    }

    suspend fun deleteUser(id: String) {
        users.document(id).delete().await()
    }


    fun listenUser(id: String) = callbackFlow {
        val listener = users.document(id)
            .addSnapshotListener { snapshot, _ ->
                trySend(snapshot?.toObject(User::class.java))
            }
        awaitClose { listener.remove() }
    }
}
