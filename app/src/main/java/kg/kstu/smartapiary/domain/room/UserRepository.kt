package kg.kstu.smartapiary.domain.room


import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

open class UserRepository(protected val userDao: UserDao, private val firebaseAuth: FirebaseAuth) {

    suspend fun getUser(): User? = userDao.getUserById(firebaseAuth.currentUser?.uid ?: "")

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun deleteUser() {
        userDao.deleteUser()
        firebaseAuth.signOut()
    }

    open suspend fun login(email: String, password: String): Boolean {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                val user = User(it.uid, it.email ?: "")
                insertUser(user) // Сохраняем в Room
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }


    open suspend fun register(email: String, password: String): Boolean {
        return false
    }
}

