package kg.kstu.smartapiary.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kg.kstu.smartapiary.presentation.screens.auth.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kg.kstu.smartapiary.presentation.screens.auth.viewmodel.AuthState


@Composable
fun RegisterScreen(viewModel: AuthViewModel = viewModel()) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Text(text = "Register", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.register(email, password)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Register")
        }

        when (authState) {
            is AuthState.Success -> {
                LaunchedEffect(authState) {
                    Toast.makeText(context, "Registration successful", Toast.LENGTH_LONG).show()
                    // Navigate to the next screen or reset the registration form
                }
            }
            is AuthState.Error -> {
                Text("Error: ${(authState as AuthState.Error).message}", color = Color.Red)
            }
            null -> {}
        }
    }
}
