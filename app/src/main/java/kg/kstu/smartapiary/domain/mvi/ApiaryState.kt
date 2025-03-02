package kg.kstu.smartapiary.domain.mvi

import kg.kstu.smartapiary.domain.data.ApiaryData

sealed class ApiaryState {
    object Loading : ApiaryState()
    data class Success(val hives: List<ApiaryData>) : ApiaryState()
    data class Error(val message: String) : ApiaryState()
}
