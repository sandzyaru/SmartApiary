package kg.kstu.smartapiary.domain.room

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kg.kstu.smartapiary.domain.User
import kg.kstu.smartapiary.domain.UserDao

open class UserRepository(private val userDao: UserDao) {
    suspend fun getUser(userId: String): User? = userDao.getUserById(userId)
    open suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun deleteUser() = userDao.deleteUser()
    open suspend fun login(email: String, password: String): Task<AuthResult> {
        TODO("Not yet implemented")
    }

    open suspend fun register(email: String, password: String): Task<AuthResult> {
        TODO("Not yet implemented")
    }
}
