package kg.kstu.smartapiary.presentation.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.kstu.smartapiary.domain.room.UserRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    suspend fun login(email: String, password: String): Boolean {
        return userRepository.login(email, password)
    }

    suspend fun register(email: String, password: String): Boolean {
        return userRepository.register(email, password)
    }

    suspend fun isUserLoggedIn(): Boolean {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val localUser = userRepository.getUser()
        return firebaseUser != null || localUser != null
    }
}
