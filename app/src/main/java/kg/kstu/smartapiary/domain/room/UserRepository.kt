package kg.kstu.smartapiary.domain.room

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kg.kstu.smartapiary.domain.User
import kg.kstu.smartapiary.domain.UserDao

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

open class UserRepository(private val userDao: UserDao, private val firebaseAuth: FirebaseAuth) {

    suspend fun getUser(): User? = userDao.getUserById(firebaseAuth.currentUser?.uid ?: "")

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun deleteUser() {
        userDao.deleteUser()
        firebaseAuth.signOut()
    }

    open suspend fun login(email: String, password: String): Boolean {
        return false // Базовая заглушка, переопределяется в FirebaseUserRepository
    }

    open suspend fun register(email: String, password: String): Boolean {
        return false // Базовая заглушка, переопределяется в FirebaseUserRepository
    }
}

