package kg.kstu.smartapiary.presentation.case

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kg.kstu.smartapiary.domain.room.UserRepository

class RegisterUseCase(private val userRepository: UserRepository) {
    suspend fun execute(email: String, password: String): Task<AuthResult> {
        return userRepository.register(email, password)
    }
}
