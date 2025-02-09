package kg.kstu.smartapiary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppWithSplashScreen {
                // После завершения загрузки, запускаем MainActivity
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                // Завершаем SplashActivity, чтобы оно не оставалось в стеке
                finish()
            }
        }
    }
}

@Composable
fun AppWithSplashScreen(onSplashFinished: () -> Unit) {
    var showSplash by rememberSaveable { mutableStateOf(true) }

    // Переход с экрана загрузки на основной экран
    if (showSplash) {
        SplashScreen {
            showSplash = false // После завершения загрузки
            onSplashFinished() // Вызываем onSplashFinished, чтобы инициировать переход
        }
    } else {
        ApiaryScreen() // Основной экран с данными улья
    }
}

@Composable
private fun SplashScreen(onSplashFinished: () -> Unit) {
    // Имитируем загрузку данных
    LaunchedEffect(Unit) {
        delay(3000) // 3 секунды
        onSplashFinished() // После задержки вызываем onSplashFinished
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)), // Цвет фона
        contentAlignment = Alignment.Center
    ) {
        // Логотип экрана загрузки
        Image(
            painter = painterResource(id = R.drawable.ic_splash), // Логотип экрана загрузки
            contentDescription = "Splash Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}
