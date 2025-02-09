package kg.kstu.smartapiary.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kg.kstu.smartapiary.presentation.screens.ApiaryScreen
import kg.kstu.smartapiary.presentation.screens.DiaryScreen
import kg.kstu.smartapiary.presentation.screens.SettingsScreen


sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Главная", Icons.Filled.Home)
    object Profile : Screen("profile", "Профиль", Icons.Filled.Person)
    object Settings : Screen("settings", "Настройки", Icons.Filled.Settings)
}

val bottomNavItems = listOf(Screen.Home, Screen.Profile, Screen.Settings)

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Home.route) { ApiaryScreen() }
        composable(Screen.Profile.route) { DiaryScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}

