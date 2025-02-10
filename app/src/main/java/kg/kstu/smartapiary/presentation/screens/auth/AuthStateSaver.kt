package kg.kstu.smartapiary.presentation.screens.auth

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

val AuthStateSaver: Saver<AuthState, Any> = listSaver(
    save = { listOf(it.email, it.password, it.isLoading, it.errorMessage, it.isLoggedIn) },
    restore = { AuthState(it[0] as String, it[1] as String, it[2] as Boolean, it[3] as? String, it[4] as Boolean) }
)
