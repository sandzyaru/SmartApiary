package kg.kstu.smartapiary.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kg.kstu.smartapiary.domain.User
import kg.kstu.smartapiary.domain.UserDao
import kg.kstu.smartapiary.domain.room.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository(userDao: UserDao, private val firebaseAuth: FirebaseAuth) :
    UserRepository(userDao, firebaseAuth) {

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                insertUser(User(it.uid, it.email ?: ""))
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun register(email: String, password: String): Boolean {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let {
                insertUser(User(it.uid, it.email ?: ""))
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
}



