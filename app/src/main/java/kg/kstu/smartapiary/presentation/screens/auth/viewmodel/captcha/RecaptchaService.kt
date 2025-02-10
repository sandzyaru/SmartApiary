package kg.kstu.smartapiary.presentation.screens.auth.viewmodel.captcha

import android.content.Context
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.safetynet.SafetyNetApi
import kotlinx.coroutines.tasks.await
import android.util.Log

suspend fun getRecaptchaToken(context: Context): String {
    return try {
        val response = SafetyNet.getClient(context)
            .verifyWithRecaptcha("AIzaSyDS1UTSm74QHftKfypkqcioeTi0ConKLeg") // Замените "YOUR_SITE_KEY" на ваш реальный API-ключ
            .await() // Асинхронное ожидание результата

        val token = response.tokenResult
        if (!token.isNullOrEmpty()) {
            token
        } else {
            Log.e("Recaptcha", "reCAPTCHA token is empty")
            ""
        }
    } catch (e: Exception) {
        Log.e("Recaptcha", "Error getting reCAPTCHA token: ${e.message}")
        ""
    }
}

