package kg.kstu.smartapiary.domain.data

data class DeviceData(
    val mac: String,
    val temp: String? = null,
    val humid: String? = null,
    val signal: String? = null,
    val weight: String? = null,
    val pressure: String? = null,
    val altitude: String? = null
)
