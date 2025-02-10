package kg.kstu.smartapiary.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kg.kstu.smartapiary.presentation.navigation.AppNavHost
import kg.kstu.smartapiary.presentation.navigation.BottomNavigationBar


@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

