package kg.kstu.smartapiary.presentation.navigation

import androidx.annotation.DrawableRes
import kg.kstu.smartapiary.R


sealed class Screen(val route: String, @DrawableRes val icon: Int, val title: String) {
    object Apiary : Screen("apiary", R.drawable.ic_honeycomb, "Пасеки")
    object Diary : Screen("diary", R.drawable.ic_diary, "Дневник")
    object Settings : Screen("settings", R.drawable.ic_settings, "Настройки")

    companion object {
        val items = listOf(Apiary, Diary, Settings)
    }
}

