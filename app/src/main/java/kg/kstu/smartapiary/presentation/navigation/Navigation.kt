package kg.kstu.smartapiary.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kg.kstu.smartapiary.presentation.screens.ApiaryScreen
import kg.kstu.smartapiary.presentation.screens.DiaryScreen
import kg.kstu.smartapiary.presentation.screens.SettingsScreen
import kg.kstu.smartapiary.presentation.screens.auth.RegisterScreen


sealed class Screen(val route: String, val icon: ImageVector, val title: String) {
    object Apiary : Screen("apiary", Icons.Default.Home, "Главная")
    object Diary : Screen("diary", Icons.Default.Person, "Профиль")
    object Settings : Screen("settings", Icons.Default.Settings, "Настройки")

    companion object {
        val items = listOf(Apiary, Diary, Settings)
    }
}

