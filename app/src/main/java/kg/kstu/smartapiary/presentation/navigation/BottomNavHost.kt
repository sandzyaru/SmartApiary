package kg.kstu.smartapiary.presentation.navigation

import androidx.compose.runtime.Composable
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
import kg.kstu.smartapiary.presentation.screens.apiary.ApiaryScreen
import kg.kstu.smartapiary.presentation.screens.apiary.add_device.AddDeviceDialog
import kg.kstu.smartapiary.presentation.screens.apiary_detail.ApiaryDetailsScreen
import kg.kstu.smartapiary.presentation.screens.diary.DiaryScreen
import kg.kstu.smartapiary.presentation.screens.settings.SettingsScreen
import kg.kstu.smartapiary.presentation.screens.viewmodel.ApiaryViewModel

@Composable
fun BottomNavHost(navController: NavHostController, mainNavController: NavHostController) { // ✅ Передаем `mainNavController`
    val apiaryViewModel: ApiaryViewModel = hiltViewModel()
    var showDialog by remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = "apiary") {
        composable("apiary") {
            ApiaryScreen(
                viewModel = apiaryViewModel,
                onHiveClick = { hiveId -> navController.navigate("apiaryDetails/$hiveId") },
                onAddDeviceClick = { showDialog = true }
            )
        }

        composable("diary") {
            DiaryScreen()
        }

        composable("settings") {
            SettingsScreen(mainNavController)
        }

        composable(
            route = "apiaryDetails/{hiveId}",
            arguments = listOf(navArgument("hiveId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("hiveId")?.let { hiveId ->
                ApiaryDetailsScreen(hiveId, navController)
            }
        }
    }

    if (showDialog) {
        AddDeviceDialog(
            viewModel = apiaryViewModel,
            onDismiss = { showDialog = false }
        )
    }
}


