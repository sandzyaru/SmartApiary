package kg.kstu.smartapiary

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kg.kstu.smartapiary.presentation.MainScreen
import kg.kstu.smartapiary.presentation.screens.auth.AuthScreen
import kg.kstu.smartapiary.presentation.screens.auth.viewmodel.AuthState
import kg.kstu.smartapiary.presentation.screens.auth.viewmodel.AuthViewModel
import kg.kstu.smartapiary.presentation.screens.auth.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                ApiaryScreen()
            }
           // MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    // Получаем FirebaseAuth (например, через зависимость или из контекста)
    val firebaseAuth = FirebaseAuth.getInstance()

    // Используем фабрику для создания AuthViewModel
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(firebaseAuth))

    val userState by authViewModel.authState.collectAsState()

    NavHost(navController, startDestination = if (userState is AuthState.Success) "main" else "auth") {
        composable("auth") { AuthScreen(authViewModel, navController) }
        composable("main") { MainScreen(navController) }
    }
}



@Composable
fun ApiaryScreen(viewModel: ApiaryViewModel = viewModel(factory = ApiaryViewModelFactory(FirebaseRepository()))) {
    val apiaryData by viewModel.apiaryData.collectAsState()


    Log.d("FirebaseDebug", "ApiaryScreen: Current data: $apiaryData")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFCFCFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_apiary), // Логотип ic_bee.jpg
                contentDescription = "Apiary",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            DataCard(
                title = "Temperature",
                value = "${apiaryData.temperature ?: "N/A"} °C",
                iconRes = R.drawable.ic_temperature
            )
            DataCard(
                title = "Humidity",
                value = "${apiaryData.humidity ?: "N/A"} %",
                iconRes = R.drawable.ic_humidity
            )
            DataCard(
                title = "Signal Strength",
                value = "${apiaryData.signal ?: "N/A"} dB",
                iconRes = R.drawable.ic_microphone
            )
            DataCard(
                title = "Pressure",
                value = "${apiaryData.pressure ?: "N/A"} gPa",
                iconRes = R.drawable.ic_pressure
            )
            DataCard(
                title = "Altitude",
                value = "${apiaryData.altitude ?: "N/A"} m",
                iconRes = R.drawable.ic_altitude
            )
            DataCard(
                title = "Weight",
                value = "${apiaryData.weight ?: "N/A"} kg",
                iconRes = R.drawable.ic_weight
            )


            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (apiaryData.connect) "Connected" else "Disconnected",
                color = if (apiaryData.connect) Color(0xFF4CAF50) else Color(0xFFF44336),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun DataCard(title: String, value: String, iconRes: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}





