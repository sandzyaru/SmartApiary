@file:OptIn(ExperimentalMaterial3Api::class)

package kg.kstu.smartapiary.presentation.screens.apiary_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kg.kstu.smartapiary.presentation.screens.viewmodel.ApiaryDetailsScreenViewModel

@Composable
fun ApiaryDetailsScreen(hiveId: String, navController: NavHostController) {
    val viewModel: ApiaryDetailsScreenViewModel = hiltViewModel()
    val graphicsData by viewModel.graphicsData.collectAsState()

    val titles = mapOf(
        "humidity" to "Влажность (%)",
        "strength_signal" to "Уровень сигнала (dB)",
        "temp" to "Температура (°C)",
        "weight" to "Вес (кг)"
    )

    val units = mapOf(
        "humidity" to "%",
        "strength_signal" to "dB",
        "temp" to "°C",
        "weight" to "кг"
    )

    LaunchedEffect(hiveId) { viewModel.loadGraphicsData(hiveId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Детали пасеки №$hiveId") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Графики параметров", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            graphicsData.forEach { (param, values) ->
                val title = titles[param] ?: param
                val unit = units[param] ?: ""
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                LineChartCanvas(values.values.map { it.toFloat() }, Color.Yellow, unit)
            }
        }
    }
}