package kg.kstu.smartapiary.domain.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kg.kstu.smartapiary.domain.room.User
import kg.kstu.smartapiary.domain.room.UserDao
import kg.kstu.smartapiary.domain.room.UserRepository
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository(userDao: UserDao, private val firebaseAuth: FirebaseAuth, private val firebaseDatabase: FirebaseDatabase) :
    UserRepository(userDao, firebaseAuth) {

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                val existingUser = userDao.getUserById(firebaseUser.uid)

                if (existingUser == null) {
                    val newUser = User(firebaseUser.uid, firebaseUser.email ?: "")
                    insertUser(newUser) // Сохраняем только если его нет
                    Log.d("UserRepository", "Пользователь ${firebaseUser.email} добавлен в Room")
                } else {
                    Log.d("UserRepository", "Пользователь ${firebaseUser.email} уже есть в Room, сохранение не требуется")
                }

                true
            } ?: false
        } catch (e: Exception) {
            Log.e("UserRepository", "Ошибка авторизации: ${e.message}", e)
            false
        }
    }

    override suspend fun register(email: String, password: String): Boolean {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                val uid = user.uid

                insertUser(User(uid, user.email ?: ""))

                val userRef = firebaseDatabase.getReference("users").child(uid)
                userRef.setValue(
                    mapOf(
                        "email" to email,
                        "hives" to emptyMap<String, Any>()
                    )
                ).await()

                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

}



