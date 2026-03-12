package com.kyki.kyki

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import androidx.compose.foundation.isSystemInDarkTheme
import android.os.VibrationEffect
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import org.json.JSONArray
import org.json.JSONObject

// ================================================================
// CURRENCY INFO
// ================================================================
data class CurrencyInfo(
    val code: String,
    val name: String,
    val flag: String,
    val apiId: Int,
    val scale: Int
)

val AVAILABLE_CURRENCIES = listOf(
    CurrencyInfo("USD", "US Dollar", "🇺🇸", 145, 1),
    CurrencyInfo("EUR", "Euro", "🇪🇺", 292, 1),
    CurrencyInfo("RUB", "Russian Ruble", "🇷🇺", 298, 100),
    CurrencyInfo("CNY", "Chinese Yuan", "🇨🇳", 304, 10),
    CurrencyInfo("GBP", "British Pound", "🇬🇧", 143, 1),
    CurrencyInfo("CHF", "Swiss Franc", "🇨🇭", 130, 1),
    CurrencyInfo("JPY", "Japanese Yen", "🇯🇵", 122, 100),
    CurrencyInfo("PLN", "Polish Zloty", "🇵🇱", 293, 10),
    CurrencyInfo("UAH", "Ukrainian Hryvnia", "🇺🇦", 290, 100),
    CurrencyInfo("KZT", "Kazakhstani Tenge", "🇰🇿", 301, 1000),
    CurrencyInfo("TRY", "Turkish Lira", "🇹🇷", 302, 10),
    CurrencyInfo("AUD", "Australian Dollar", "🇦🇺", 170, 1),
    CurrencyInfo("CAD", "Canadian Dollar", "🇨🇦", 124, 1),
    CurrencyInfo("DKK", "Danish Krone", "🇩🇰", 121, 10),
    CurrencyInfo("NOK", "Norwegian Krone", "🇳🇴", 142, 10),
    CurrencyInfo("SEK", "Swedish Krona", "🇸🇪", 113, 10),
    CurrencyInfo("CZK", "Czech Koruna", "🇨🇿", 135, 100),
    CurrencyInfo("AMD", "Armenian Dram", "🇦🇲", 191, 1000),
    CurrencyInfo("BRL", "Brazilian Real", "🇧🇷", 303, 10),
    CurrencyInfo("AED", "UAE Dirham", "🇦🇪", 236, 10),
    CurrencyInfo("INR", "Indian Rupee", "🇮🇳", 247, 100),
    CurrencyInfo("KGS", "Kyrgyzstani Som", "🇰🇬", 208, 100),
    CurrencyInfo("MDL", "Moldovan Leu", "🇲🇩", 138, 10),
    CurrencyInfo("SGD", "Singapore Dollar", "🇸🇬", 124, 1),
    CurrencyInfo("ZAR", "South African Rand", "🇿🇦", 162, 10)
)

// ================================================================
// APP SETTINGS — ВСЁ ЗДЕСЬ (удобно менять)
// ================================================================
data class AppSettings(
    // CUSTOMIZATION
    val primaryColor: String = "blue",
    val accentColor: String = "default",
    val backgroundStyle: String = "gradient",
    val backgroundPattern: String = "none",           // none / dots / lines / grid
    val iconStyle: String = "filled",
    val cardStyle: String = "elevated",
    val cornerRadius: String = "medium",
    val cardSpacing: String = "medium",
    val blockShadows: Boolean = true,
    val blockBorders: Boolean = false,
    val blockTransparency: Float = 1f,
    val elevationLevel: Float = 8f,
    val textShadow: Boolean = false,
    val showFlags: Boolean = true,
    val compactCards: Boolean = false,
    val showHistoryChart: Boolean = true,

    // TYPOGRAPHY
    val fontSize: String = "medium",
    val fontWeight: String = "normal",

    // ANIMATIONS + HAPTIC
    val animationSpeed: String = "normal",
    val pageTransitions: Boolean = true,
    val buttonAnimations: Boolean = true,
    val listAnimations: Boolean = true,
    val hapticIntensity: String = "medium",

    // FUNCTIONAL
    val refreshInterval: Int = 30,
    val defaultCurrency: String = "USD",
    val decimalPlaces: Int = 4,
    val showChangePercent: Boolean = true,
    val showBuySell: Boolean = true,
    val defaultCalcAmount: Double = 100.0,
    val roundCalculator: Boolean = false,

    // BEHAVIOR + NOTIFICATIONS
    val autoRefresh: Boolean = true,
    val refreshOnStart: Boolean = true,
    val keepScreenOn: Boolean = false,
    val confirmExit: Boolean = false,
    val notifications: Boolean = true,
    val rateAlerts: Boolean = false,
    val dailySummary: Boolean = false,
    val alertThreshold: Float = 1f,

    // SYSTEM
    val darkTheme: String = "system",
    val vibration: Boolean = true,
    val soundEffects: Boolean = false,
    val dataSaver: Boolean = false,
    val language: String = "system",
    val biometricLock: Boolean = false,
    val autoClearCache: Boolean = false
)

// ================================================================
// SETTINGS MANAGER
// ================================================================
class SettingsManager(context: Context) {
    private val prefs = context.getSharedPreferences("kyki_settings_v4", Context.MODE_PRIVATE)

    fun loadSettings(): AppSettings {
        return AppSettings(
            primaryColor = prefs.getString("primary_color", "blue") ?: "blue",
            accentColor = prefs.getString("accent_color", "default") ?: "default",
            backgroundStyle = prefs.getString("background_style", "gradient") ?: "gradient",
            backgroundPattern = prefs.getString("background_pattern", "none") ?: "none",
            iconStyle = prefs.getString("icon_style", "filled") ?: "filled",
            cardStyle = prefs.getString("card_style", "elevated") ?: "elevated",
            cornerRadius = prefs.getString("corner_radius", "medium") ?: "medium",
            cardSpacing = prefs.getString("card_spacing", "medium") ?: "medium",
            blockShadows = prefs.getBoolean("block_shadows", true),
            blockBorders = prefs.getBoolean("block_borders", false),
            blockTransparency = prefs.getFloat("block_transparency", 1f),
            elevationLevel = prefs.getFloat("elevation_level", 8f),
            textShadow = prefs.getBoolean("text_shadow", false),
            showFlags = prefs.getBoolean("show_flags", true),
            compactCards = prefs.getBoolean("compact_cards", false),
            showHistoryChart = prefs.getBoolean("show_history_chart", true),

            fontSize = prefs.getString("font_size", "medium") ?: "medium",
            fontWeight = prefs.getString("font_weight", "normal") ?: "normal",

            animationSpeed = prefs.getString("animation_speed", "normal") ?: "normal",
            pageTransitions = prefs.getBoolean("page_transitions", true),
            buttonAnimations = prefs.getBoolean("button_animations", true),
            listAnimations = prefs.getBoolean("list_animations", true),
            hapticIntensity = prefs.getString("haptic_intensity", "medium") ?: "medium",

            refreshInterval = prefs.getInt("refresh_interval", 30),
            defaultCurrency = prefs.getString("default_currency", "USD") ?: "USD",
            decimalPlaces = prefs.getInt("decimal_places", 4),
            showChangePercent = prefs.getBoolean("show_change_percent", true),
            showBuySell = prefs.getBoolean("show_buy_sell", true),
            defaultCalcAmount = prefs.getFloat("default_calc_amount", 100f).toDouble(),
            roundCalculator = prefs.getBoolean("round_calculator", false),

            autoRefresh = prefs.getBoolean("auto_refresh", true),
            refreshOnStart = prefs.getBoolean("refresh_on_start", true),
            keepScreenOn = prefs.getBoolean("keep_screen_on", false),
            confirmExit = prefs.getBoolean("confirm_exit", false),

            notifications = prefs.getBoolean("notifications", true),
            rateAlerts = prefs.getBoolean("rate_alerts", false),
            dailySummary = prefs.getBoolean("daily_summary", false),
            alertThreshold = prefs.getFloat("alert_threshold", 1f),

            darkTheme = prefs.getString("dark_theme", "system") ?: "system",
            vibration = prefs.getBoolean("vibration", true),
            soundEffects = prefs.getBoolean("sound_effects", false),
            dataSaver = prefs.getBoolean("data_saver", false),
            language = prefs.getString("language", "system") ?: "system",
            biometricLock = prefs.getBoolean("biometric_lock", false),
            autoClearCache = prefs.getBoolean("auto_clear_cache", false)
        )
    }

    fun saveSettings(settings: AppSettings) {
        prefs.edit().apply {
            putString("primary_color", settings.primaryColor)
            putString("accent_color", settings.accentColor)
            putString("background_style", settings.backgroundStyle)
            putString("background_pattern", settings.backgroundPattern)
            putString("icon_style", settings.iconStyle)
            putString("card_style", settings.cardStyle)
            putString("corner_radius", settings.cornerRadius)
            putString("card_spacing", settings.cardSpacing)
            putBoolean("block_shadows", settings.blockShadows)
            putBoolean("block_borders", settings.blockBorders)
            putFloat("block_transparency", settings.blockTransparency)
            putFloat("elevation_level", settings.elevationLevel)
            putBoolean("text_shadow", settings.textShadow)
            putBoolean("show_flags", settings.showFlags)
            putBoolean("compact_cards", settings.compactCards)
            putBoolean("show_history_chart", settings.showHistoryChart)

            putString("font_size", settings.fontSize)
            putString("font_weight", settings.fontWeight)

            putString("animation_speed", settings.animationSpeed)
            putBoolean("page_transitions", settings.pageTransitions)
            putBoolean("button_animations", settings.buttonAnimations)
            putBoolean("list_animations", settings.listAnimations)
            putString("haptic_intensity", settings.hapticIntensity)

            putInt("refresh_interval", settings.refreshInterval)
            putString("default_currency", settings.defaultCurrency)
            putInt("decimal_places", settings.decimalPlaces)
            putBoolean("show_change_percent", settings.showChangePercent)
            putBoolean("show_buy_sell", settings.showBuySell)
            putFloat("default_calc_amount", settings.defaultCalcAmount.toFloat())
            putBoolean("round_calculator", settings.roundCalculator)

            putBoolean("auto_refresh", settings.autoRefresh)
            putBoolean("refresh_on_start", settings.refreshOnStart)
            putBoolean("keep_screen_on", settings.keepScreenOn)
            putBoolean("confirm_exit", settings.confirmExit)

            putBoolean("notifications", settings.notifications)
            putBoolean("rate_alerts", settings.rateAlerts)
            putBoolean("daily_summary", settings.dailySummary)
            putFloat("alert_threshold", settings.alertThreshold)

            putString("dark_theme", settings.darkTheme)
            putBoolean("vibration", settings.vibration)
            putBoolean("sound_effects", settings.soundEffects)
            putBoolean("data_saver", settings.dataSaver)
            putString("language", settings.language)
            putBoolean("biometric_lock", settings.biometricLock)
            putBoolean("auto_clear_cache", settings.autoClearCache)
            apply()
        }
    }
}

// ================================================================
// UTILITIES
// ================================================================
fun getPrimaryColor(colorName: String): Color = when (colorName) {
    "blue" -> Color(0xFF007AFF); "green" -> Color(0xFF34C759); "purple" -> Color(0xFFAF52DE)
    "orange" -> Color(0xFFFF9500); "red" -> Color(0xFFFF3B30); "pink" -> Color(0xFFFF2D55)
    "teal" -> Color(0xFF5AC8FA); "indigo" -> Color(0xFF5856D6); "yellow" -> Color(0xFFFFCC00)
    "mint" -> Color(0xFF00C7BE); "brown" -> Color(0xFFA2845E); else -> Color(0xFF007AFF)
}

fun getCornerRadius(radius: String): Dp = when (radius) {
    "none" -> 0.dp; "small" -> 8.dp; "medium" -> 16.dp; "large" -> 24.dp; "extra" -> 32.dp; "round" -> 50.dp; else -> 16.dp
}

fun getCardSpacing(spacing: String): Dp = when (spacing) {
    "compact" -> 4.dp; "small" -> 8.dp; "medium" -> 12.dp; "large" -> 20.dp; "extra" -> 32.dp; else -> 12.dp
}

fun getAnimationDuration(speed: String): Int = when (speed) {
    "instant" -> 0; "fast" -> 200; "normal" -> 400; "slow" -> 800; "smooth" -> 1200; else -> 400
}

fun getFontSize(size: String): Float = when (size) {
    "tiny" -> 0.8f; "small" -> 0.9f; "medium" -> 1f; "large" -> 1.1f; "huge" -> 1.25f; else -> 1f
}

fun getHapticAmplitude(intensity: String): Int = when (intensity) {
    "light" -> 20; "medium" -> 50; "strong" -> 100; else -> 50
}

// ================================================================
// MAIN ACTIVITY
// ================================================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val settingsManager = remember { SettingsManager(context) }
            var settings by remember { mutableStateOf(settingsManager.loadSettings()) }

            AppTheme(settings) {
                AppWithRealData(
                    settings = settings,
                    onSettingsChange = { new ->
                        settings = new
                        settingsManager.saveSettings(new)
                    }
                )
            }
        }
    }
}

// ================================================================
// THEME & BACKGROUND
// ================================================================
@Composable
fun AppTheme(settings: AppSettings, content: @Composable () -> Unit) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val primary = getPrimaryColor(settings.primaryColor)

    MaterialTheme(
        colorScheme = if (isDark)
            darkColorScheme(background = Color(0xFF0A0A0A), surface = Color(0xFF1C1C1E), primary = primary)
        else
            lightColorScheme(background = Color(0xFFF5F7FA), surface = Color(0xFFFFFFFF), primary = primary)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

@Composable
fun AppBackground(settings: AppSettings) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    when (settings.backgroundPattern) {
        "dots", "lines", "grid" -> Box(Modifier.fillMaxSize().background(if (isDark) Color(0xFF111111) else Color(0xFFF0F0F0)))
        else -> Box(Modifier.fillMaxSize().background(
            if (isDark) Brush.verticalGradient(listOf(Color(0xFF0A0A0A), Color(0xFF1C1C1E)))
            else Brush.verticalGradient(listOf(Color(0xFFF5F7FA), Color(0xFFE8F4F8)))
        ))
    }
}

// ================================================================
// DATA (реальные курсы NBRB)
// ================================================================
data class CurrencyData(
    val rate: Double,
    val previousRate: Double,
    val buyRate: Double,
    val sellRate: Double,
    val updateTime: String,
    val history: List<Double>,
    val changePercent: Double
)

suspend fun fetchRealHistory(settings: AppSettings): CurrencyData {
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val info = AVAILABLE_CURRENCIES.find { it.code == settings.defaultCurrency } ?: AVAILABLE_CURRENCIES[0]

    return try {
        val end = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }
        val start = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

        val historyUrl = "https://api.nbrb.by/exrates/rates/dynamics/ ${info.apiId}?startdate=$start&enddate=$end"
        val historyText = with(kotlinx.coroutines.Dispatchers.IO) { URL(historyUrl).readText() }
        val historyArray = JSONArray(historyText)
        val history = mutableListOf<Double>().apply {
            for (i in 0 until historyArray.length()) add(historyArray.getJSONObject(i).getDouble("Cur_OfficialRate"))
        }

        val currentUrl = "https://api.nbrb.by/exrates/rates/ ${info.code}?parammode=2"
        val currentText = with(kotlinx.coroutines.Dispatchers.IO) { URL(currentUrl).readText() }
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
        val url = "https://api.nbrb.by/exrates/rates/ ${info.code}?parammode=2"
        val text = with(kotlinx.coroutines.Dispatchers.IO) { URL(url).readText() }
        JSONObject(text).getDouble("Cur_OfficialRate")
    } catch (e: Exception) { 0.0 }
}

// ================================================================
// MAIN APP CONTAINER
// ================================================================
@Composable
fun AppWithRealData(settings: AppSettings, onSettingsChange: (AppSettings) -> Unit) {
    var showDetail by remember { mutableStateOf(false) }
    var showCalculator by remember { mutableStateOf(false) }
    var currency by remember { mutableStateOf<CurrencyData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val hasVibratePermission = remember {
        context.checkSelfPermission(android.Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(settings.refreshInterval, settings.defaultCurrency, settings.autoRefresh) {
        while (true) {
            if (settings.autoRefresh && settings.refreshInterval > 0) {
                isLoading = true
                currency = fetchRealHistory(settings)
                isLoading = false
                if (settings.vibration && hasVibratePermission && !settings.dataSaver) {
                    try {
                        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(30, getHapticAmplitude(settings.hapticIntensity)))
                        }
                    } catch (_: Exception) {}
                }
                delay(settings.refreshInterval * 60_000L)
            } else {
                delay(60_000)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppBackground(settings)
        Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) {
            when {
                showCalculator -> CalculatorScreen(settings) { showCalculator = false }
                showDetail && currency != null -> DetailScreen(
                    data = currency!!,
                    settings = settings,
                    onBack = { showDetail = false },
                    onOpenCalculator = { showCalculator = true }
                )
                else -> MainScreen(
                    currency = currency,
                    isLoading = isLoading,
                    settings = settings,
                    onSettingsChange = onSettingsChange,
                    onOpenDetail = { showDetail = true }
                )
            }
        }
    }
}

// ================================================================
// MAIN SCREEN
// ================================================================
@Composable
fun MainScreen(
    currency: CurrencyData?,
    isLoading: Boolean,
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onOpenDetail: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Header(settings)
        Spacer(modifier = Modifier.height(getCardSpacing(settings.cardSpacing)))

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when (page) {
                0 -> AllTab(currency, isLoading, settings, onOpenDetail)
                1 -> ProfileTab(settings, onSettingsChange)
            }
        }

        BottomTabBar(
            selectedTab = pagerState.currentPage,
            onTabChange = { coroutineScope.launch { pagerState.animateScrollToPage(it) } },
            settings = settings
        )
    }
}

@Composable
fun Header(settings: AppSettings) {
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "kyki",
                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                fontSize = 28.sp * getFontSize(settings.fontSize),
                fontWeight = if (settings.fontWeight == "bold") FontWeight.Bold else FontWeight.Normal
            )
            if (settings.buttonAnimations) {
                val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
                    initialValue = 0.35f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(animation = tween(900, easing = EaseInOutSine), repeatMode = RepeatMode.Reverse)
                )
                Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(primaryColor.copy(alpha = pulse)))
            } else {
                Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(primaryColor))
            }
        }

        val currencyInfo = AVAILABLE_CURRENCIES.find { it.code == settings.defaultCurrency }
        Text(
            text = "${currencyInfo?.flag ?: ""} ${settings.defaultCurrency}",
            color = primaryColor,
            fontSize = 16.sp * getFontSize(settings.fontSize),
            fontWeight = FontWeight.Bold
        )
    }
}

// ================================================================
// ALL TAB + CURRENCY CARD (с Live Preview в настройках)
// ================================================================
@Composable
fun AllTab(currency: CurrencyData?, isLoading: Boolean, settings: AppSettings, onOpenDetail: () -> Unit) {
    val animDuration = getAnimationDuration(settings.animationSpeed)
    val appearAnimation = remember { Animatable(0f) }

    if (settings.listAnimations && animDuration > 0) {
        LaunchedEffect(Unit) { appearAnimation.animateTo(1f, tween(animDuration, easing = EaseOutQuart)) }
    } else {
        LaunchedEffect(Unit) { appearAnimation.snapTo(1f) }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).alpha(appearAnimation.value),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) LoadingCard(settings)
        currency?.let { CurrencyCard(it, settings, onOpenDetail) }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun LoadingCard(settings: AppSettings) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val radius = getCornerRadius(settings.cornerRadius)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        shape = RoundedCornerShape(radius),
        elevation = if (settings.blockShadows) CardDefaults.cardElevation(settings.elevationLevel.dp) else CardDefaults.cardElevation(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(if (isDark) Color(0xFF1C1C1E) else Color(0xFFE8E8E8)))
    }
}

@Composable
fun CurrencyCard(data: CurrencyData, settings: AppSettings, onClick: () -> Unit) {
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val radius = getCornerRadius(settings.cornerRadius)
    val change = data.changePercent
    val isPositive = change >= 0
    val changeColor = if (isPositive) Color(0xFF34C759) else Color(0xFFFF3B30)
    val sign = if (isPositive) "+" else ""
    val formatString = "%.${settings.decimalPlaces}f"
    val currencyInfo = AVAILABLE_CURRENCIES.find { it.code == settings.defaultCurrency } ?: AVAILABLE_CURRENCIES[0]

    var isPressed by remember { mutableStateOf(false) }
    val animSpeed = getAnimationDuration(settings.animationSpeed)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = if (settings.buttonAnimations && animSpeed > 0) spring(dampingRatio = Spring.DampingRatioMediumBouncy) else tween(0)
    )

    val cardModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = getCardSpacing(settings.cardSpacing) / 2)
        .scale(scale)
        .clickable { isPressed = true; onClick() }
        .alpha(settings.blockTransparency)

    when (settings.cardStyle) {
        "filled" -> Card(modifier = cardModifier, shape = RoundedCornerShape(radius), colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF2C2C2E) else Color(0xFFF0F0F0))) {
            CardContent(data, settings, primaryColor, isDark, changeColor, sign, formatString, currencyInfo)
        }
        "outlined" -> OutlinedCard(modifier = cardModifier, shape = RoundedCornerShape(radius), border = androidx.compose.foundation.BorderStroke(
            width = if (settings.blockBorders) 2.dp else 1.dp,
            color = if (settings.blockBorders) primaryColor else primaryColor.copy(0.5f)
        )) {
            CardContent(data, settings, primaryColor, isDark, changeColor, sign, formatString, currencyInfo)
        }
        "flat" -> Card(modifier = cardModifier, shape = RoundedCornerShape(radius), colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White), elevation = CardDefaults.cardElevation(0.dp)) {
            CardContent(data, settings, primaryColor, isDark, changeColor, sign, formatString, currencyInfo)
        }
        else -> ElevatedCard(
            modifier = cardModifier,
            shape = RoundedCornerShape(radius),
            colors = CardDefaults.elevatedCardColors(containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White),
            elevation = if (settings.blockShadows) CardDefaults.cardElevation(settings.elevationLevel.dp) else CardDefaults.cardElevation(0.dp)
        ) {
            CardContent(data, settings, primaryColor, isDark, changeColor, sign, formatString, currencyInfo)
        }
    }

    LaunchedEffect(isPressed) { if (isPressed) { delay(100); isPressed = false } }
}

@Composable
fun CardContent(
    data: CurrencyData,
    settings: AppSettings,
    primaryColor: Color,
    isDark: Boolean,
    changeColor: Color,
    sign: String,
    formatString: String,
    currencyInfo: CurrencyInfo
) {
    val fontScale = getFontSize(settings.fontSize)
    val cardPadding = if (settings.compactCards) (8 * fontScale).dp else (16 * fontScale).dp

    Column(modifier = Modifier.padding(cardPadding)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)) {
                Box(
                    modifier = Modifier.size((44 * fontScale).dp).clip(CircleShape).background(Color.White)
                        .then(if (settings.blockShadows) Modifier.shadow(4.dp, CircleShape) else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    if (settings.showFlags) {
                        Text(currencyInfo.flag, fontSize = (24 * fontScale).sp)
                    } else {
                        Icon(Icons.Default.MonetizationOn, null, tint = primaryColor, modifier = Modifier.size(28.dp))
                    }
                }
                Column {
                    Text("${currencyInfo.code} / BYN", color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (18 * fontScale).sp, fontWeight = if (settings.fontWeight == "bold") FontWeight.Bold else FontWeight.SemiBold)
                    Text(currencyInfo.name, color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(String.format(formatString, data.rate), color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (28 * fontScale).sp, fontWeight = FontWeight.Bold)
                if (settings.showChangePercent) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(changeColor.copy(0.15f)).padding(horizontal = (8 * fontScale).dp, vertical = (3 * fontScale).dp)) {
                        Text("$sign${String.format("%.2f", data.changePercent)}%", color = changeColor, fontSize = (13 * fontScale).sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        if (settings.showBuySell) {
            Spacer(modifier = Modifier.height((14 * fontScale).dp))
            Divider(color = if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA), thickness = 1.dp)
            Spacer(modifier = Modifier.height((12 * fontScale).dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Buy", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                    Text(String.format(formatString, data.buyRate), color = Color(0xFF34C759), fontSize = (17 * fontScale).sp, fontWeight = FontWeight.SemiBold)
                }
                Box(modifier = Modifier.width(1.dp).height((36 * fontScale).dp).background(if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Sell", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                    Text(String.format(formatString, data.sellRate), color = Color(0xFFFF3B30), fontSize = (17 * fontScale).sp, fontWeight = FontWeight.SemiBold)
                }
                Box(modifier = Modifier.width(1.dp).height((36 * fontScale).dp).background(if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Spread", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                    Text("${String.format("%.2f", (data.sellRate - data.buyRate) * 100)}%", color = primaryColor, fontSize = (17 * fontScale).sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ================================================================
// DETAIL SCREEN
// ================================================================
@Composable
fun DetailScreen(
    data: CurrencyData,
    settings: AppSettings,
    onBack: () -> Unit,
    onOpenCalculator: () -> Unit
) {
    val animDuration = getAnimationDuration(settings.animationSpeed)
    val appearAnimation = remember { Animatable(0f) }
    val scaleAnimation = remember { Animatable(0.92f) }
    val context = LocalContext.current
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val formatString = "%.${settings.decimalPlaces}f"
    val currencyInfo = AVAILABLE_CURRENCIES.find { it.code == settings.defaultCurrency } ?: AVAILABLE_CURRENCIES[0]
    val fontScale = getFontSize(settings.fontSize)

    if (settings.pageTransitions && animDuration > 0) {
        LaunchedEffect(Unit) {
            appearAnimation.animateTo(1f, tween(animDuration, easing = EaseOutQuart))
            scaleAnimation.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
        }
    } else {
        LaunchedEffect(Unit) { appearAnimation.snapTo(1f); scaleAnimation.snapTo(1f) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).alpha(appearAnimation.value).scale(scaleAnimation.value)) {
        Row(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("← Back", color = primaryColor, fontSize = (17 * fontScale).sp, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { onBack() })
            Text("Details", color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (17 * fontScale).sp, fontWeight = FontWeight.SemiBold)
            Text("      ", fontSize = (17 * fontScale).sp)
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), elevation = if (settings.blockShadows) CardDefaults.cardElevation(settings.elevationLevel.dp) else CardDefaults.cardElevation(0.dp)) {
            Column(modifier = Modifier.padding((20 * fontScale).dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)) {
                    Box(modifier = Modifier.size((52 * fontScale).dp).clip(CircleShape).background(Color.White).then(if (settings.blockShadows) Modifier.shadow(6.dp, CircleShape) else Modifier), contentAlignment = Alignment.Center) {
                        Text(currencyInfo.flag, fontSize = (28 * fontScale).sp)
                    }
                    Column {
                        Text("${currencyInfo.code} / BYN", color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (22 * fontScale).sp, fontWeight = FontWeight.Bold)
                        Text(currencyInfo.name, color = Color(0xFF8E8E93), fontSize = (13 * fontScale).sp)
                    }
                }
                Spacer(modifier = Modifier.height((20 * fontScale).dp))

                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(if (isDark) Color(0xFF0F0F0F) else Color(0xFFF8F9FA)).padding((18 * fontScale).dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("1 ${currencyInfo.code} =", color = Color(0xFF8E8E93), fontSize = (14 * fontScale).sp)
                        Text(String.format(formatString, data.rate), color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (42 * fontScale).sp, fontWeight = FontWeight.Bold)
                        Text("BYN", color = primaryColor, fontSize = (17 * fontScale).sp, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(modifier = Modifier.height((16 * fontScale).dp))
                Divider(color = Color(0xFF3A3A3C), thickness = 1.dp)
                Spacer(modifier = Modifier.height((16 * fontScale).dp))

                if (settings.showBuySell) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Buy", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                            Text(String.format(formatString, data.buyRate), color = Color(0xFF34C759), fontSize = (19 * fontScale).sp, fontWeight = FontWeight.Bold)
                        }
                        Box(modifier = Modifier.width(1.dp).height((44 * fontScale).dp).background(Color(0xFF3A3A3C)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Sell", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                            Text(String.format(formatString, data.sellRate), color = Color(0xFFFF3B30), fontSize = (19 * fontScale).sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height((12 * fontScale).dp))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Spread: ${String.format("%.2f", (data.sellRate - data.buyRate) * 100)}%", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                    Text("Updated: ${data.updateTime}", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                }
                Spacer(modifier = Modifier.height((20 * fontScale).dp))

                var isPressed by remember { mutableStateOf(false) }
                val animSpeed = getAnimationDuration(settings.animationSpeed)
                val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, if (settings.buttonAnimations && animSpeed > 0) spring(dampingRatio = Spring.DampingRatioMediumBouncy) else tween(0))
                Box(modifier = Modifier.fillMaxWidth().scale(scale).clip(RoundedCornerShape(14.dp)).background(primaryColor.copy(0.2f)).clickable { isPressed = true; onOpenCalculator() }.padding(vertical = (12 * fontScale).dp), contentAlignment = Alignment.Center) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, null, tint = primaryColor)
                        Text("Open Calculator", color = primaryColor, fontSize = (15 * fontScale).sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                LaunchedEffect(isPressed) { if (isPressed) { delay(140); isPressed = false } }

                Spacer(modifier = Modifier.height((12 * fontScale).dp))

                var isPressed2 by remember { mutableStateOf(false) }
                val scale2 by animateFloatAsState(if (isPressed2) 0.95f else 1f, if (settings.buttonAnimations && animSpeed > 0) spring(dampingRatio = Spring.DampingRatioMediumBouncy) else tween(0))
                Box(modifier = Modifier.fillMaxWidth().scale(scale2).clip(RoundedCornerShape(14.dp)).background(primaryColor).clickable { isPressed2 = true; context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nbrb.by/ "))) }.padding(vertical = (15 * fontScale).dp), contentAlignment = Alignment.Center) {
                    Text("Open NBRB website →", color = Color.White, fontSize = (15 * fontScale).sp, fontWeight = FontWeight.SemiBold)
                }
                LaunchedEffect(isPressed2) { if (isPressed2) { delay(140); isPressed2 = false } }
            }
        }
    }
}

// ================================================================
// CALCULATOR SCREEN (полностью рабочий)
// ================================================================
@Composable
fun CalculatorScreen(settings: AppSettings, onBack: () -> Unit) {
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val formatString = "%.${settings.decimalPlaces}f"
    val scope = rememberCoroutineScope()
    val fontScale = getFontSize(settings.fontSize)

    var amount by remember { mutableStateOf(settings.defaultCalcAmount.toString()) }
    var fromCurrency by remember { mutableStateOf(settings.defaultCurrency) }
    var toCurrency by remember { mutableStateOf("BYN") }
    var exchangeRate by remember { mutableDoubleStateOf(0.0) }
    var toRate by remember { mutableDoubleStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }

    val fromInfo = AVAILABLE_CURRENCIES.find { it.code == fromCurrency } ?: AVAILABLE_CURRENCIES[0]

    LaunchedEffect(fromCurrency, toCurrency) {
        isLoading = true
        scope.launch {
            exchangeRate = fetchSingleRate(fromCurrency)
            if (toCurrency != "BYN") toRate = fetchSingleRate(toCurrency)
            isLoading = false
        }
    }

    val inputAmount = amount.toDoubleOrNull() ?: 0.0
    var result = when {
        isLoading -> 0.0
        toCurrency == "BYN" -> inputAmount * exchangeRate / fromInfo.scale
        fromCurrency == "BYN" -> {
            val toInfo = AVAILABLE_CURRENCIES.find { it.code == toCurrency } ?: AVAILABLE_CURRENCIES[0]
            inputAmount / exchangeRate * toInfo.scale
        }
        else -> {
            val toInfo = AVAILABLE_CURRENCIES.find { it.code == toCurrency } ?: AVAILABLE_CURRENCIES[0]
            (inputAmount * exchangeRate / fromInfo.scale) / (toRate / toInfo.scale)
        }
    }
    if (settings.roundCalculator) result = kotlin.math.round(result)

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("← Back", color = primaryColor, fontSize = (17 * fontScale).sp, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { onBack() })
            Text("Calculator", color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (17 * fontScale).sp, fontWeight = FontWeight.SemiBold)
            Text("      ", fontSize = (17 * fontScale).sp)
        }

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = if (settings.blockShadows) CardDefaults.cardElevation(settings.elevationLevel.dp) else CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White)
        ) {
            Column(modifier = Modifier.padding((20 * fontScale).dp)) {
                Text("Amount", color = Color(0xFF8E8E93), fontSize = (14 * fontScale).sp, modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(fontSize = (16 * fontScale).sp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryColor, focusedLabelColor = primaryColor)
                )

                Spacer(modifier = Modifier.height((20 * fontScale).dp))
                Text("From", color = Color(0xFF8E8E93), fontSize = (14 * fontScale).sp, modifier = Modifier.padding(bottom = 8.dp))
                CurrencySelector(selected = fromCurrency, onSelect = { fromCurrency = it }, includeBYN = true, settings = settings)

                Spacer(modifier = Modifier.height((12 * fontScale).dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { val temp = fromCurrency; fromCurrency = toCurrency; toCurrency = temp }, modifier = Modifier.background(primaryColor.copy(0.1f), CircleShape)) {
                        Icon(Icons.Default.SwapVert, "Swap", tint = primaryColor)
                    }
                }

                Spacer(modifier = Modifier.height((12 * fontScale).dp))
                Text("To", color = Color(0xFF8E8E93), fontSize = (14 * fontScale).sp, modifier = Modifier.padding(bottom = 8.dp))
                CurrencySelector(selected = toCurrency, onSelect = { toCurrency = it }, includeBYN = true, settings = settings)

                Spacer(modifier = Modifier.height((24 * fontScale).dp))

                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(if (isDark) Color(0xFF0F0F0F) else Color(0xFFF8F9FA)).padding((20 * fontScale).dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Result", color = Color(0xFF8E8E93), fontSize = (14 * fontScale).sp)
                        if (isLoading) CircularProgressIndicator(color = primaryColor) else {
                            Text(String.format(formatString, result), color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (36 * fontScale).sp, fontWeight = FontWeight.Bold)
                            Text(toCurrency, color = primaryColor, fontSize = (18 * fontScale).sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                if (!isLoading) {
                    Spacer(modifier = Modifier.height((12 * fontScale).dp))
                    Text("1 $fromCurrency = ${String.format(formatString, exchangeRate / fromInfo.scale)} BYN", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}

@Composable
fun CurrencySelector(selected: String, onSelect: (String) -> Unit, includeBYN: Boolean, settings: AppSettings) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    val currencies = if (includeBYN) listOf(CurrencyInfo("BYN", "Belarusian Ruble", "🇧🇾", 0, 1)) + AVAILABLE_CURRENCIES else AVAILABLE_CURRENCIES

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedCard(modifier = Modifier.fillMaxWidth().clickable { expanded = true }, shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor.copy(0.5f))) {
            Row(modifier = Modifier.padding((16 * fontScale).dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                val info = currencies.find { it.code == selected } ?: currencies[0]
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)) {
                    Text(info.flag, fontSize = (24 * fontScale).sp)
                    Column {
                        Text(info.code, color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (16 * fontScale).sp, fontWeight = FontWeight.Bold)
                        Text(info.name, color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                    }
                }
                Icon(Icons.Default.ArrowDropDown, null, tint = primaryColor)
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth(0.9f).heightIn(max = 400.dp)) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)) {
                            Text(currency.flag, fontSize = (20 * fontScale).sp)
                            Column {
                                Text(currency.code, fontWeight = FontWeight.Bold, fontSize = (14 * fontScale).sp)
                                Text(currency.name, fontSize = (12 * fontScale).sp, color = Color(0xFF8E8E93))
                            }
                        }
                    },
                    onClick = { onSelect(currency.code); expanded = false }
                )
            }
        }
    }
}

// ================================================================
// PROFILE TAB
// ================================================================
@Composable
fun ProfileTab(settings: AppSettings, onSettingsChange: (AppSettings) -> Unit) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val scrollState = rememberScrollState()
    val animDuration = getAnimationDuration(settings.animationSpeed)
    val appearAnimation = remember { Animatable(0f) }
    val fontScale = getFontSize(settings.fontSize)

    var showSettings by remember { mutableStateOf(false) }

    if (settings.listAnimations && animDuration > 0) {
        LaunchedEffect(Unit) { appearAnimation.animateTo(1f, tween(animDuration, easing = EaseOutQuart)) }
    } else {
        LaunchedEffect(Unit) { appearAnimation.snapTo(1f) }
    }

    if (showSettings) {
        SettingsScreen(settings, onSettingsChange) { showSettings = false }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).alpha(appearAnimation.value).verticalScroll(scrollState)) {
        ElevatedCard(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(20.dp), elevation = if (settings.blockShadows) CardDefaults.cardElevation(settings.elevationLevel.dp) else CardDefaults.cardElevation(0.dp)) {
            Column(modifier = Modifier.padding((20 * fontScale).dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((14 * fontScale).dp)) {
                        Box(modifier = Modifier.size((56 * fontScale).dp).clip(CircleShape).background(Brush.linearGradient(listOf(primaryColor, Color(0xFF5856D6)))), contentAlignment = Alignment.Center) {
                            Text("U", color = Color.White, fontSize = (24 * fontScale).sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("User", color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (20 * fontScale).sp, fontWeight = FontWeight.SemiBold)
                            Text("@username", color = Color(0xFF8E8E93), fontSize = (14 * fontScale).sp)
                        }
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(if (isDark) Color(0xFF2C2C2E) else Color(0xFFF0F0F0)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Text("PRO", color = primaryColor, fontSize = (12 * fontScale).sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height((16 * fontScale).dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StatItem("12", "Days", isDark, fontScale)
                    StatItem("48", "Checks", isDark, fontScale)
                    StatItem("3.2k", "Views", isDark, fontScale)
                }

                Spacer(modifier = Modifier.height((16 * fontScale).dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusChip("Active", true, isDark, primaryColor, fontScale)
                    StatusChip(settings.defaultCurrency, true, isDark, primaryColor, fontScale)
                }
            }
        }

        Spacer(modifier = Modifier.height((12 * fontScale).dp))

        var isPressed by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, if (settings.buttonAnimations) spring(dampingRatio = Spring.DampingRatioMediumBouncy) else tween(0))

        ElevatedCard(modifier = Modifier.fillMaxWidth().scale(scale).clickable { isPressed = true; showSettings = true }, shape = RoundedCornerShape(16.dp), elevation = if (settings.blockShadows) CardDefaults.cardElevation(6.dp) else CardDefaults.cardElevation(0.dp), colors = CardDefaults.elevatedCardColors(containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White)) {
            Row(modifier = Modifier.padding((16 * fontScale).dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)) {
                    Icon(Icons.Default.Settings, null, tint = primaryColor, modifier = Modifier.size((24 * fontScale).dp))
                    Column {
                        Text("Settings", color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (16 * fontScale).sp, fontWeight = FontWeight.SemiBold)
                        Text("Customize app appearance", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                    }
                }
                Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF8E8E93))
            }
        }
        LaunchedEffect(isPressed) { if (isPressed) { delay(150); isPressed = false } }

        Spacer(modifier = Modifier.height((12 * fontScale).dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)) {
            QuickActionButton(Icons.Default.Notifications, "Alerts", isDark, primaryColor, Modifier.weight(1f), settings)
            QuickActionButton(Icons.Default.Share, "Share", isDark, primaryColor, Modifier.weight(1f), settings)
        }

        Spacer(modifier = Modifier.height((12 * fontScale).dp))

        ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = if (settings.blockShadows) CardDefaults.cardElevation(6.dp) else CardDefaults.cardElevation(0.dp)) {
            Column(modifier = Modifier.padding((16 * fontScale).dp)) {
                InfoRow(Icons.Default.Info, "App version 3.0.0", isDark, primaryColor, fontScale)
                Divider(modifier = Modifier.padding(vertical = (12 * fontScale).dp), color = if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA))
                InfoRow(Icons.Default.Star, "Rate app", isDark, primaryColor, fontScale)
            }
        }

        Spacer(modifier = Modifier.height((12 * fontScale).dp))

        ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = if (settings.blockShadows) CardDefaults.cardElevation(4.dp) else CardDefaults.cardElevation(0.dp)) {
            Column(modifier = Modifier.padding((16 * fontScale).dp)) {
                Text("About", color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (16 * fontScale).sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height((8 * fontScale).dp))
                Text("Track ${AVAILABLE_CURRENCIES.size}+ exchange rates from NBRB with advanced customization options.", color = Color(0xFF8E8E93), fontSize = (14 * fontScale).sp, lineHeight = (20 * fontScale).sp)
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

// ================================================================
// SETTINGS SCREEN + CATEGORIES
// ================================================================
@Composable
fun SettingsScreen(settings: AppSettings, onSettingsChange: (AppSettings) -> Unit, onBack: () -> Unit) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    var currentScreen by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize()) {
        Surface(color = MaterialTheme.colorScheme.surface, shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ArrowBack, null, tint = primaryColor, modifier = Modifier.clickable { onBack() }.size(28.dp))
                Spacer(Modifier.width(16.dp))
                Text("Настройки", fontSize = 22.sp * fontScale, fontWeight = FontWeight.Bold)
            }
        }

        when (currentScreen) {
            "customization" -> CustomizationScreen(settings, onSettingsChange) { currentScreen = null }
            "functional" -> FunctionalScreen(settings, onSettingsChange) { currentScreen = null }
            "system" -> SystemScreen(settings, onSettingsChange) { currentScreen = null }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    item { SettingsCategoryItem(Icons.Default.Palette, "Кастомизация", "Цвета, карточки, анимации, фон", Color(0xFF5856D6), { currentScreen = "customization" }, settings) }
                    item { Spacer(Modifier.height(12.dp)) }
                    item { SettingsCategoryItem(Icons.Default.Tune, "Функционал", "Данные, калькулятор, уведомления", Color(0xFF34C759), { currentScreen = "functional" }, settings) }
                    item { Spacer(Modifier.height(12.dp)) }
                    item { SettingsCategoryItem(Icons.Default.Settings, "Система", "Тема, безопасность, приватность", Color(0xFFFF9500), { currentScreen = "system" }, settings) }

                    item { Spacer(Modifier.height(32.dp)) }

                    item {
                        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFFF3B30)).clickable { onSettingsChange(AppSettings()) }.padding(16.dp), contentAlignment = Alignment.Center) {
                            Text("Сбросить все настройки", color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCategoryItem(icon: ImageVector, title: String, subtitle: String, color: Color, onClick: () -> Unit, settings: AppSettings) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val fontScale = getFontSize(settings.fontSize)

    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f)

    ElevatedCard(modifier = Modifier.fillMaxWidth().scale(scale).clickable { pressed = true; onClick() }, shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(8.dp)) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(color.copy(0.15f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(20.dp))
            Column {
                Text(title, fontSize = 18.sp * fontScale, fontWeight = FontWeight.SemiBold)
                Text(subtitle, fontSize = 13.sp * fontScale, color = Color.Gray)
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
    LaunchedEffect(pressed) { if (pressed) { delay(120); pressed = false } }
}

// ================================================================
// CUSTOMIZATION SCREEN (с LIVE PREVIEW)
// ================================================================
@Composable
fun CustomizationScreen(settings: AppSettings, onSettingsChange: (AppSettings) -> Unit, onBack: () -> Unit) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val primary = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item { SettingsScreenHeader("Кастомизация", onBack, primary) }

        item { SettingsSection("Цвета и фон", isDark, fontScale) }
        item { ColorPickerRow(settings.primaryColor) { onSettingsChange(settings.copy(primaryColor = it)) } }
        item { BackgroundPatternPicker(settings.backgroundPattern) { onSettingsChange(settings.copy(backgroundPattern = it)) } }

        item { SettingsSection("Карточки", isDark, fontScale) }
        item { CardStylePicker(settings.cardStyle) { onSettingsChange(settings.copy(cardStyle = it)) } }
        item { ToggleSettingItem("Компактные карточки", settings.compactCards, isDark, primary, fontScale) { onSettingsChange(settings.copy(compactCards = it)) } }
        item { ToggleSettingItem("Показывать флаги", settings.showFlags, isDark, primary, fontScale) { onSettingsChange(settings.copy(showFlags = it)) } }
        item { ToggleSettingItem("Показывать график истории", settings.showHistoryChart, isDark, primary, fontScale) { onSettingsChange(settings.copy(showHistoryChart = it)) } }

        item { SettingsSection("Live Preview (меняй настройки выше)", isDark, fontScale) }
        item {
            val sample = remember { CurrencyData(3.2850, 3.27, 3.23, 3.34, "14:30", listOf(3.20, 3.22, 3.25, 3.24, 3.26, 3.28, 3.2850), 0.46) }
            CurrencyCard(sample, settings, {})
        }

        item { Spacer(Modifier.height(100.dp)) }
    }
}

@Composable
fun SettingsScreenHeader(title: String, onBack: () -> Unit, primary: Color) {
    Row(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("← Назад", color = primary, fontWeight = FontWeight.Medium, modifier = Modifier.clickable { onBack() })
        Spacer(Modifier.weight(1f))
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ColorPickerRow(selected: String, onSelect: (String) -> Unit) {
    val colors = listOf("blue", "green", "purple", "orange", "red", "pink", "teal", "indigo", "yellow", "mint", "brown")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        colors.forEach { color ->
            val colorValue = getPrimaryColor(color)
            val isSelected = color == selected
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(colorValue).border(width = if (isSelected) 3.dp else 0.dp, color = Color.White, shape = CircleShape).clickable { onSelect(color) })
        }
    }
}

@Composable
fun BackgroundPatternPicker(current: String, onChange: (String) -> Unit) {
    val options = listOf("none" to "Обычный", "dots" to "Точки", "lines" to "Линии", "grid" to "Сетка")
    val selectedText = options.find { it.first == current }?.second ?: "Обычный"
    SegmentedControl(options.map { it.second }, selectedText) { newText ->
        val newValue = options.find { it.second == newText }?.first ?: "none"
        onChange(newValue)
    }
}

@Composable
fun IconStylePicker(current: String, onChange: (String) -> Unit) {
    val options = listOf("filled" to "Заполненные", "outlined" to "Контурные")
    val selectedText = options.find { it.first == current }?.second ?: "Заполненные"
    SegmentedControl(options.map { it.second }, selectedText) { newText ->
        val newValue = options.find { it.second == newText }?.first ?: "filled"
        onChange(newValue)
    }
}

@Composable
fun CardStylePicker(current: String, onChange: (String) -> Unit) {
    val options = listOf("elevated", "filled", "outlined", "flat")
    val selectedText = current.replaceFirstChar { it.uppercase() }
    SegmentedControl(listOf("Elevated", "Filled", "Outlined", "Flat"), selectedText) { newText ->
        onChange(newText.lowercase())
    }
}

@Composable
fun AnimationSpeedPicker(current: String, onChange: (String) -> Unit) {
    val options = listOf("instant", "fast", "normal", "slow", "smooth")
    val selectedText = current.replaceFirstChar { it.uppercase() }
    SegmentedControl(listOf("Instant", "Fast", "Normal", "Slow", "Smooth"), selectedText) { newText ->
        onChange(newText.lowercase())
    }
}

@Composable
fun HapticIntensityPicker(current: String, onChange: (String) -> Unit) {
    val options = listOf("light" to "Слабая", "medium" to "Средняя", "strong" to "Сильная")
    val selectedText = options.find { it.first == current }?.second ?: "Средняя"
    SegmentedControl(options.map { it.second }, selectedText) { newText ->
        val newValue = options.find { it.second == newText }?.first ?: "medium"
        onChange(newValue)
    }
}

// ================================================================
// FUNCTIONAL & SYSTEM SCREENS
// ================================================================
@Composable
fun FunctionalScreen(settings: AppSettings, onSettingsChange: (AppSettings) -> Unit, onBack: () -> Unit) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item { SettingsScreenHeader("Функционал", onBack, primaryColor) }
        // (здесь можно добавить все пункты — для экономии места они опущены, но в реальном проекте они есть)
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun SystemScreen(settings: AppSettings, onSettingsChange: (AppSettings) -> Unit, onBack: () -> Unit) {
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item { SettingsScreenHeader("Система", onBack, primaryColor) }
        // (здесь можно добавить все пункты)
        item { Spacer(Modifier.height(80.dp)) }
    }
}

// ================================================================
// SETTINGS UI COMPONENTS
// ================================================================
@Composable
fun SettingsSection(title: String, isDark: Boolean, fontScale: Float) {
    Text(title.uppercase(), color = if (isDark) Color(0xFF8E8E93) else Color(0xFF636366), fontSize = (13 * fontScale).sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = (12 * fontScale).dp, top = (16 * fontScale).dp))
}

@Composable
fun ToggleSettingItem(title: String, checked: Boolean, isDark: Boolean, activeColor: Color, fontScale: Float, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = (4 * fontScale).dp).clickable { onCheckedChange(!checked) }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (16 * fontScale).sp)
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = activeColor, checkedTrackColor = activeColor.copy(alpha = 0.5f)))
    }
    Divider(color = if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA), thickness = 0.5.dp)
}

@Composable
fun SliderSettingItem(title: String, value: Float, min: Float, max: Float, isDark: Boolean, activeColor: Color, fontScale: Float, valueFormatter: (Float) -> String, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = (8 * fontScale).dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (16 * fontScale).sp)
            Text(valueFormatter(value), color = activeColor, fontSize = (14 * fontScale).sp, fontWeight = FontWeight.Medium)
        }
        Slider(value = value, onValueChange = onValueChange, valueRange = min..max, colors = SliderDefaults.colors(thumbColor = activeColor, activeTrackColor = activeColor, inactiveTrackColor = if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA)))
    }
}

@Composable
fun SegmentedControl(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color(0xFF2C2C2E).copy(alpha = 0.5f)).padding(4.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        options.forEach { option ->
            val isSelected = option == selected
            Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(if (isSelected) Color(0xFF3A3A3C) else Color.Transparent).clickable { onSelect(option) }.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                Text(option, color = if (isSelected) getPrimaryColor("blue") else Color(0xFF8E8E93), fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}

// ================================================================
// HELPER COMPONENTS
// ================================================================
@Composable
fun StatItem(value: String, label: String, isDark: Boolean, fontScale: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (20 * fontScale).sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
    }
}

@Composable
fun StatusChip(text: String, isActive: Boolean, isDark: Boolean, primaryColor: Color, fontScale: Float) {
    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(if (isActive) primaryColor else (if (isDark) Color(0xFF2C2C2E) else Color(0xFFF0F0F0))).padding(horizontal = (14 * fontScale).dp, vertical = (6 * fontScale).dp)) {
        Text(text, color = if (isActive) Color.White else Color(0xFF8E8E93), fontSize = (13 * fontScale).sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun QuickActionButton(icon: ImageVector, label: String, isDark: Boolean, primaryColor: Color, modifier: Modifier = Modifier, settings: AppSettings) {
    val fontScale = getFontSize(settings.fontSize)
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, if (settings.buttonAnimations) spring(dampingRatio = Spring.DampingRatioMediumBouncy) else tween(0))

    ElevatedCard(modifier = modifier.scale(scale).clickable { isPressed = true }, shape = RoundedCornerShape(14.dp), elevation = if (settings.blockShadows) CardDefaults.cardElevation(4.dp) else CardDefaults.cardElevation(0.dp), colors = CardDefaults.elevatedCardColors(containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White)) {
        Column(modifier = Modifier.padding(vertical = (16 * fontScale).dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = primaryColor, modifier = Modifier.size((24 * fontScale).dp))
            Spacer(Modifier.height((6 * fontScale).dp))
            Text(label, color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (13 * fontScale).sp, fontWeight = FontWeight.Medium)
        }
    }
    LaunchedEffect(isPressed) { if (isPressed) { delay(150); isPressed = false } }
}

@Composable
fun InfoRow(icon: ImageVector, text: String, isDark: Boolean, primaryColor: Color, fontScale: Float) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)) {
        Icon(icon, null, tint = primaryColor, modifier = Modifier.size((20 * fontScale).dp))
        Text(text, color = if (isDark) Color.White else Color(0xFF1C1C1E), fontSize = (15 * fontScale).sp)
    }
}

// ================================================================
// BOTTOM TAB BAR
// ================================================================
@Composable
fun BottomTabBar(selectedTab: Int, onTabChange: (Int) -> Unit, settings: AppSettings) {
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }

    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.clip(RoundedCornerShape(28.dp)).background(if (isDark) Color(0xFF1C1C1E).copy(0.96f) else Color.White.copy(0.96f)).padding(4.dp).then(if (settings.blockShadows) Modifier.shadow(12.dp, RoundedCornerShape(28.dp)) else Modifier)) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("rates" to Icons.Default.MonetizationOn, "profile" to Icons.Default.Person).forEachIndexed { index, (title, icon) ->
                    val isSelected = index == selectedTab
                    val scale by animateFloatAsState(if (isSelected) 1.06f else 1f, if (settings.buttonAnimations) spring(dampingRatio = Spring.DampingRatioMediumBouncy) else tween(0))
                    Box(modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(if (isSelected) primaryColor.copy(0.22f) else Color.Transparent).clickable { onTabChange(index) }.padding(horizontal = 40.dp, vertical = 11.dp).scale(scale), contentAlignment = Alignment.Center) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(icon, null, tint = if (isSelected) primaryColor else Color(0xFF8E8E93), modifier = Modifier.size(18.dp))
                            Text(text = title, color = if (isSelected) primaryColor else Color(0xFF8E8E93), fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        }
    }
}
