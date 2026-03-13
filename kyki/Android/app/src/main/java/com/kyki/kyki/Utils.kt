package com.kyki.kyki

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

// Цвета
fun getPrimaryColor(colorName: String): Color = when (colorName) {
    "blue" -> Color(0xFF007AFF)
    "green" -> Color(0xFF34C759)
    "purple" -> Color(0xFFAF52DE)
    "orange" -> Color(0xFFFF9500)
    "red" -> Color(0xFFFF3B30)
    "pink" -> Color(0xFFFF2D55)
    "teal" -> Color(0xFF5AC8FA)
    "indigo" -> Color(0xFF5856D6)
    "yellow" -> Color(0xFFFFCC00)
    "mint" -> Color(0xFF00C7BE)
    "brown" -> Color(0xFFA2845E)
    else -> Color(0xFF007AFF)
}

// Размеры
fun getCornerRadius(radius: String): Dp = when (radius) {
    "none" -> 0.dp
    "small" -> 8.dp
    "medium" -> 16.dp
    "large" -> 24.dp
    "extra" -> 32.dp
    "round" -> 50.dp
    else -> 16.dp
}

fun getCardSpacing(spacing: String): Dp = when (spacing) {
    "compact" -> 4.dp
    "small" -> 8.dp
    "medium" -> 12.dp
    "large" -> 20.dp
    "extra" -> 32.dp
    else -> 12.dp
}

// Анимации
fun getAnimationDuration(speed: String): Int = when (speed) {
    "instant" -> 0
    "fast" -> 200
    "normal" -> 400
    "slow" -> 800
    "smooth" -> 1200
    else -> 400
}

// Шрифты
fun getFontSize(size: String): Float = when (size) {
    "tiny" -> 0.8f
    "small" -> 0.9f
    "medium" -> 1f
    "large" -> 1.1f
    "huge" -> 1.25f
    else -> 1f
}

// Haptic
fun getHapticAmplitude(intensity: String): Int = when (intensity) {
    "light" -> 20
    "medium" -> 50
    "strong" -> 100
    else -> 50
}

// Форматирование цены: для целых чисел без десятичных знаков
fun formatPrice(value: Double, decimalPlaces: Int): String {
    return if (value % 1 == 0.0) {
        "%.0f".format(value)
    } else {
        String.format("%.${decimalPlaces}f", value)
    }
}

// Загрузка данных из NBRB
suspend fun fetchRealHistory(settings: AppSettings): CurrencyData {
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val info = AVAILABLE_CURRENCIES.find { it.code == settings.defaultCurrency } ?: AVAILABLE_CURRENCIES[0]

    return try {
        val end = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }
        val start = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

        val historyUrl = "https://api.nbrb.by/exrates/rates/dynamics/${info.apiId}?startdate=$start&enddate=$end"
        val historyText = withContext(Dispatchers.IO) { URL(historyUrl).readText() }
        val historyArray = JSONArray(historyText)
        val history = mutableListOf<Double>().apply {
            for (i in 0 until historyArray.length()) add(historyArray.getJSONObject(i).getDouble("Cur_OfficialRate"))
        }

        val currentUrl = "https://api.nbrb.by/exrates/rates/${info.code}?parammode=2"
        val currentText = withContext(Dispatchers.IO) { URL(currentUrl).readText() }
        val current = JSONObject(currentText).getDouble("Cur_OfficialRate")
        val yesterday = if (history.size > 1) history[history.size - 2] else current * 0.995
        val change = ((current - yesterday) / yesterday) * 100

        CurrencyData(current, yesterday, current * 0.985, current * 1.015, time, history, change)
    } catch (e: Exception) {
        val base = 3.2850
        CurrencyData(base, base * 0.992, base * 0.985, base * 1.015, time, listOf(3.20, 3.22, 3.25, 3.24, 3.26, 3.28, base), 0.8)
    }
}

suspend fun fetchSingleRate(code: String): Double {
    if (code == "BYN") return 1.0
    val info = AVAILABLE_CURRENCIES.find { it.code == code } ?: return 0.0
    return try {
        val url = "https://api.nbrb.by/exrates/rates/${info.code}?parammode=2"
        val text = withContext(Dispatchers.IO) { URL(url).readText() }
        JSONObject(text).getDouble("Cur_OfficialRate")
    } catch (e: Exception) { 0.0 }
}
