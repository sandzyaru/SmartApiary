package kg.kstu.smartapiary.presentation.screens.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.kstu.smartapiary.domain.data.ApiaryData
import kg.kstu.smartapiary.domain.data.DeviceData
import kg.kstu.smartapiary.domain.mvi.ApiaryIntent
import kg.kstu.smartapiary.domain.mvi.ApiaryState
import kg.kstu.smartapiary.domain.repository.ApiaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiaryViewModel @Inject constructor(
    private val repository: ApiaryRepository
) : ViewModel() {

    private val _apiaryState = MutableStateFlow<ApiaryState>(ApiaryState.Loading)
    val apiaryState: StateFlow<ApiaryState> = _apiaryState.asStateFlow()

    companion object {
        private const val TAG = "ApiaryViewModel"
    }

    fun handleIntent(intent: ApiaryIntent) {
        when (intent) {
            is ApiaryIntent.LoadHives -> loadApiaryData()
        }
    }

    fun claimHiveByMac(macAddress: String) {
        viewModelScope.launch {
            _apiaryState.value = ApiaryState.Loading
            val success = repository.claimHiveByMac(macAddress)
            if (success) {
                loadApiaryData()
            } else {
                _apiaryState.value = ApiaryState.Error("Ошибка привязки пасеки")
            }
        }
    }

    private fun loadApiaryData() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Загрузка списка пасек...")
                _apiaryState.value = ApiaryState.Loading

                val hives = repository.getUserHives()

                if (hives.isEmpty()) {
                    Log.w(TAG, "Пасеки не найдены")
                    _apiaryState.value = ApiaryState.Error("Пасеки не найдены")
                    return@launch
                }

                val apiaryDataList = hives.map { hiveId ->
                    Log.d(TAG, "Загрузка устройств для улья: $hiveId")
                    val devices = repository.getHiveDevices(hiveId).map { mac ->
                        Log.d(TAG, "Загрузка данных с устройства: $mac")
                        val data = repository.getDeviceData(mac)

                        DeviceData(
                            mac = mac,
                            temp = data["temp"] ?: "N/A",
                            humid = data["humid"] ?: "N/A",
                            signal = data["strength_signal"] ?: "N/A",
                            weight = data["weight"] ?: "N/A",
                            pressure = data["pressure"] ?: "N/A",
                            altitude = data["altitude"] ?: "N/A"
                        )
                    }

                    ApiaryData(hiveId, devices)
                }

                Log.d(TAG, "Загрузка завершена, передача данных в UI")
                _apiaryState.value = ApiaryState.Success(apiaryDataList)
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка загрузки данных пасеки: ${e.message}", e)
                _apiaryState.value = ApiaryState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}

