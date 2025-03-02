package kg.kstu.smartapiary.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import kg.kstu.smartapiary.domain.repository.ApiaryRepository
import kg.kstu.smartapiary.domain.room.UserRepository
import kg.kstu.smartapiary.presentation.navigation.BottomNavHost
import kg.kstu.smartapiary.presentation.navigation.BottomNavigationBar

@Composable
fun MainScreen() {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(bottomNavController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            BottomNavHost(navController = bottomNavController)
        }
    }
}