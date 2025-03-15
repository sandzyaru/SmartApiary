package kg.kstu.smartapiary.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import kg.kstu.smartapiary.domain.mvi.ApiaryState
import kg.kstu.smartapiary.presentation.screens.viewmodel.ApiaryViewModel
import kotlinx.coroutines.launch

@Composable
fun AddDeviceDialog(
    viewModel: ApiaryViewModel,
    onDismiss: () -> Unit
) {
    var macAddress by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    val uiState by viewModel.apiaryState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        if (uiState is ApiaryState.Error) {
            errorText = (uiState as ApiaryState.Error).message
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить устройство") },
        text = {
            Column {
                Text("Введите MAC-адрес устройства:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = macAddress,
                    onValueChange = {
                        macAddress = it
                        errorText = null
                    },
                    placeholder = { Text("00:1A:7D:DA:71:13") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Characters
                    ),
                    isError = errorText != null
                )
                if (errorText != null) {
                    Text(text = errorText!!, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (macAddress.isNotEmpty()) {
                        scope.launch {
                            viewModel.claimHiveByMac(macAddress)
                        }
                    } else {
                        errorText = "Введите MAC-адрес"
                    }
                },
                enabled = macAddress.isNotEmpty() && uiState !is ApiaryState.Loading
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}