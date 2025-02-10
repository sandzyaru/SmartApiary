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

class FirebaseUserRepository(private val userDao: UserDao) : UserRepository(userDao) {

    override suspend fun register(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        val userEntity = User(user.uid, user.email ?: "")
                        // Insert the user in the local database
                        CoroutineScope(Dispatchers.IO).launch {
                            insertUser(userEntity)
                        }
                    }
                }
            }
    }

    override suspend fun login(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        val userEntity = User(user.uid, user.email ?: "")
                        // Insert the user in the local database
                        CoroutineScope(Dispatchers.IO).launch {
                            insertUser(userEntity)
                        }
                    }
                }
            }
    }
}


