package kg.kstu.smartapiary.domain

data class ApiaryData(
    val temperature: String? = null,
    val humidity: String? = null,
    val signal: String? = null,
    val weight: String? = null,
    val pressure: String? = null,
    val altitude: String? = null,
    val connect: Boolean = false
) {
    override fun toString(): String {
        return "Temperature: $temperature, Humidity: $humidity, Signal: $signal, Weight: $weight, Connected: $connect"
    }
}


