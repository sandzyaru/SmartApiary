package kg.kstu.smartapiary.domain.mvi

sealed class ApiaryIntent {
    object LoadHives : ApiaryIntent()
}