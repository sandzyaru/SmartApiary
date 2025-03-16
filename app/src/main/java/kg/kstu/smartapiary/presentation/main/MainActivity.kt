package kg.kstu.smartapiary.presentation.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.kstu.smartapiary.presentation.MainScreen
import kg.kstu.smartapiary.presentation.screens.auth.AuthScreen
import kg.kstu.smartapiary.presentation.screens.auth.RegisterScreen
import kg.kstu.smartapiary.presentation.screens.splash.SplashScreen
import kg.kstu.smartapiary.presentation.screens.viewmodel.AuthViewModel
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp(viewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    var isUserLoggedIn by rememberSaveable { mutableStateOf(false) }
    var isCheckingAuth by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isUserLoggedIn = viewModel.isUserLoggedIn()
        isCheckingAuth = false
    }

    if (isCheckingAuth) {
        SplashScreen()
    } else {
        NavHost(
            navController = navController,
            startDestination = if (isUserLoggedIn) "main" else "auth"
        ) {
            composable("auth") { AuthScreen(navController) }
            composable("register") { RegisterScreen(navController) }
            composable("main") { MainScreen(navController) }
        }
    }
}