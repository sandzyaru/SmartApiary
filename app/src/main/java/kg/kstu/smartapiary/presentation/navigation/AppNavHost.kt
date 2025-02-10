package kg.kstu.smartapiary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kg.kstu.smartapiary.domain.room.UserRepository
import kg.kstu.smartapiary.presentation.MainScreen
import kg.kstu.smartapiary.presentation.screens.auth.AuthScreen
import kg.kstu.smartapiary.presentation.screens.auth.RegisterScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    userRepository: UserRepository, // Передаём репозиторий
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) "main" else "auth",
        modifier = modifier
    ) {
        composable("auth") { AuthScreen(navController, userRepository) }
        composable("register") { RegisterScreen(navController, userRepository) }
        composable("main") { MainScreen(userRepository) }
    }
}




