package kg.kstu.smartapiary.presentation.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.kstu.smartapiary.domain.repository.ApiaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiaryDetailsScreenViewModel @Inject constructor(
    private val repository: ApiaryRepository
) : ViewModel() {

    private val _graphicsData = MutableStateFlow<Map<String, Map<String, Double>>>(emptyMap())
    val graphicsData: StateFlow<Map<String, Map<String, Double>>> = _graphicsData.asStateFlow()

    fun loadGraphicsData(hiveId: String) {
        viewModelScope.launch {
            _graphicsData.value = repository.getGraphicsData(hiveId)
        }
    }

}