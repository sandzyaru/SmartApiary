package kg.kstu.smartapiary.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kg.kstu.smartapiary.presentation.screens.auth.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kg.kstu.smartapiary.presentation.screens.auth.viewmodel.AuthState


@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavHostController
) {
    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(onClick = { viewModel.login(email, password) }) {
            Text("Login")
        }

        Button(onClick = { navController.navigate("register") }) {
            Text("Register")
        }

        when (authState) {
            is AuthState.Success -> Text("Authentication successful")
            is AuthState.Error -> Text("Error: ${(authState as AuthState.Error).message}")
            else -> {}
        }
    }
}



