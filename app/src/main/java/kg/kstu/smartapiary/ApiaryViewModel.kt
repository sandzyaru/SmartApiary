package kg.kstu.smartapiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApiaryViewModel(private val repository: FirebaseRepository) : ViewModel() {
    private val _apiaryData = MutableStateFlow(ApiaryData())
    val apiaryData: StateFlow<ApiaryData> = _apiaryData

    init {
        viewModelScope.launch {
            repository.apiaryData.observeForever { data ->
                _apiaryData.value = data
            }
            repository.loadData()
        }
    }
}

class ApiaryViewModelFactory(private val repository: FirebaseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApiaryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}