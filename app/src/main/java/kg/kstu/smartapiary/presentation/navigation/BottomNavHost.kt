package kg.kstu.smartapiary.presentation.navigation

import ApiaryDetailsScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kg.kstu.smartapiary.domain.mvi.ApiaryState
import kg.kstu.smartapiary.presentation.AddDeviceDialog
import kg.kstu.smartapiary.presentation.screens.ApiaryScreen
import kg.kstu.smartapiary.presentation.screens.DiaryScreen
import kg.kstu.smartapiary.presentation.screens.SettingsScreen
import kg.kstu.smartapiary.presentation.screens.viewmodel.ApiaryViewModel

@Composable
fun BottomNavHost(navController: NavHostController) {
    val apiaryViewModel: ApiaryViewModel = hiltViewModel()
    var showDialog by remember { mutableStateOf(false) }
    val apiaryState by apiaryViewModel.apiaryState.collectAsState()

    NavHost(navController = navController, startDestination = "apiary") {
        composable("apiary") {
            ApiaryScreen(
                viewModel = apiaryViewModel,
                onHiveClick = { hiveId -> navController.navigate("apiaryDetails/$hiveId") },
                onAddDeviceClick = { showDialog = true }
            )
        }
        composable("diary") { DiaryScreen() }
        composable("settings") { SettingsScreen() }

        composable(
            route = "apiaryDetails/{hiveId}",
            arguments = listOf(navArgument("hiveId") { type = NavType.StringType })
        ) { backStackEntry ->
            val hiveId = backStackEntry.arguments?.getString("hiveId") ?: "N/A"
            ApiaryDetailsScreen(hiveId, navController)
        }
    }

    // Проверяем, есть ли доступные пасеки
    val hasApiaries = apiaryState is ApiaryState.Success && (apiaryState as ApiaryState.Success).hives.isNotEmpty()

    if (showDialog) {
        if (hasApiaries) {
            AddDeviceDialog(
                viewModel = apiaryViewModel,
                onDismiss = { showDialog = false }
            )
        } else {
            showDialog = false // Закрываем диалог, если пасек нет
        }
    }
}

