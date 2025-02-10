package kg.kstu.smartapiary.presentation.screens.auth.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(private val firebaseAuth: FirebaseAuth) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState?>(null)
    val authState: StateFlow<AuthState?> = _authState.asStateFlow()

    // Функция для входа пользователя
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    // Функция для регистрации нового пользователя
    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }
}

sealed class AuthState {
    object Success : AuthState()
    data class Error(val message: String?) : AuthState()
}
