package kg.kstu.smartapiary.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kg.kstu.smartapiary.domain.room.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun AuthScreen(navController: NavHostController, userRepository: UserRepository) {
    var state by rememberSaveable(stateSaver = AuthStateSaver) { mutableStateOf(AuthState()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = state.email, onValueChange = { state = state.copy(email = it) }, label = { Text("Email") })

        TextField(
            value = state.password,
            onValueChange = { state = state.copy(password = it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                state = state.copy(isLoading = true, errorMessage = null)

                CoroutineScope(Dispatchers.IO).launch {
                    val success = userRepository.login(state.email, state.password)
                    withContext(Dispatchers.Main) {
                        state = state.copy(isLoading = false)
                        if (success) navController.navigate("main") else state = state.copy(errorMessage = "Ошибка входа")
                    }
                }
            },
            enabled = !state.isLoading
        ) {
            Text(if (state.isLoading) "Loading..." else "Login")
        }

        Button(onClick = { navController.navigate("register") }) { Text("Register") }

        if (state.errorMessage != null) {
            Text(text = state.errorMessage!!, color = Color.Red)
        }
    }
}






