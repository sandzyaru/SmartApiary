package kg.kstu.smartapiary.presentation.screens.apiary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kg.kstu.smartapiary.domain.mvi.ApiaryIntent
import kg.kstu.smartapiary.domain.mvi.ApiaryState
import kg.kstu.smartapiary.presentation.screens.viewmodel.ApiaryViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiaryScreen(
    viewModel: ApiaryViewModel,
    onHiveClick: (String) -> Unit,
    onAddDeviceClick: () -> Unit
) {
    val state by viewModel.apiaryState.collectAsState()

    LaunchedEffect(Unit) { viewModel.handleIntent(ApiaryIntent.LoadHives) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пасеки") },
                actions = {
                    IconButton(onClick = onAddDeviceClick) { // Открываем диалог добавления устройства
                        Icon(painter = painterResource(id = android.R.drawable.ic_input_add),
                            contentDescription = "Добавить устройство")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state) {
                is ApiaryState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ApiaryState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text((state as ApiaryState.Error).message, color = MaterialTheme.colorScheme.error)
                    }
                }

                is ApiaryState.Success -> {
                    val successState = state as ApiaryState.Success
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    ) {
                        items(successState.hives) { hive ->
                            ApiaryCard(hive, onHiveClick)
                        }
                    }
                }
            }
        }
    }
}