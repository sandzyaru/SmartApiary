package kg.kstu.smartapiary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kg.kstu.smartapiary.domain.room.UserRepository
import kg.kstu.smartapiary.presentation.screens.ApiaryScreen
import kg.kstu.smartapiary.presentation.screens.DiaryScreen
import kg.kstu.smartapiary.presentation.screens.SettingsScreen

@Composable
fun BottomNavHost(navController: NavHostController, userRepository: UserRepository) {
    NavHost(navController = navController, startDestination = Screen.Apiary.route) {
        composable(Screen.Apiary.route) { ApiaryScreen() }
        composable(Screen.Diary.route) { DiaryScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
