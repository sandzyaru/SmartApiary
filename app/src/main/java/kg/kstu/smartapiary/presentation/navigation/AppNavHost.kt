package kg.kstu.smartapiary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kg.kstu.smartapiary.presentation.screens.ApiaryScreen
import kg.kstu.smartapiary.presentation.screens.DiaryScreen
import kg.kstu.smartapiary.presentation.screens.SettingsScreen
import kg.kstu.smartapiary.presentation.screens.auth.AuthScreen
import kg.kstu.smartapiary.presentation.screens.auth.RegisterScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Home.route) { ApiaryScreen() }
        composable(Screen.Profile.route) { DiaryScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable("register") {
            RegisterScreen(viewModel = viewModel())
        }
        // Дополнительно, предоставьте navController экрану авторизации
        composable("auth") {
            AuthScreen(viewModel = viewModel(), navController = navController)
        }
    }
}

