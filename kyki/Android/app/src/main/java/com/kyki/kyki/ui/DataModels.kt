package com.kyki.kyki

// ========== МОДЕЛИ ДЛЯ ФИАТА ==========
data class CurrencyInfo(
    val code: String,
    val name: String,
    val flag: String,
    val apiId: Int,
    val scale: Int
)

data class CurrencyData(
    val rate: Double,
    val previousRate: Double,
    val buyRate: Double,
    val sellRate: Double,
    val updateTime: String,
    val history: List<Double>,
    val changePercent: Double
)

// ========== МОДЕЛИ ДЛЯ КРИПТЫ ==========
data class CryptoInfo(
    val id: String,
    val symbol: String,
    val name: String
)

data class CryptoData(
    val symbol: String,
    val rate: Double,
    val changePercent24h: Double,
    val marketCap: Double? = null,
    val volume24h: Double? = null,
    val high24h: Double? = null,
    val low24h: Double? = null,
    val updateTime: String
)

// ========== НАСТРОЙКИ ==========
data class AppSettings(
    // Основные
    val primaryColor: String = "blue",
    val accentColor: String = "default",
    val backgroundStyle: String = "gradient",
    // ===== НОВЫЕ НАСТРОЙКИ КАСТОМИЗАЦИИ =====
    val backgroundType: String = "gradient",           // "gradient", "solid", "pattern"
    val solidBackgroundColor: String = "default",      // если backgroundType = "solid"
    val gradientStart: String = "primary",             // "primary", "blue", "purple", ...
    val gradientEnd: String = "secondary",             // "secondary", "pink", "orange", ...
    val cardBorderWidth: Float = 1f,                   // толщина границы карточек
    val cardBorderStyle: String = "solid",             // "solid", "dashed", "none"
    val iconAnimation: Boolean = true,                  // анимация иконок
    val transitionEffect: String = "slide",             // "slide", "fade", "scale", "none"
    val hapticFeedbackLevel: String = "medium",         // "off", "light", "medium", "strong"
    val shimmerEffect: Boolean = true,                  // эффект мерцания при загрузке
    val parallaxEffect: Boolean = false,                // параллакс в фоне
    val customFont: String = "default",                 // "default", "monospace", "serif"
    val customCornerRadius: Int = 16,                   // отдельное значение (переопределяет cornerRadius если нужно)
    val backgroundPattern: String = "none",
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

    // Текст
    val fontSize: String = "medium",
    val fontWeight: String = "normal",

    // Анимации
    val animationSpeed: String = "normal",
    val pageTransitions: Boolean = true,
    val buttonAnimations: Boolean = true,
    val listAnimations: Boolean = true,
    val hapticIntensity: String = "medium",

    // Функциональные
    val refreshInterval: Int = 30,
    val defaultCurrency: String = "USD",
    val decimalPlaces: Int = 4,
    val showChangePercent: Boolean = true,
    val showBuySell: Boolean = true,
    val defaultCalcAmount: Double = 100.0,
    val roundCalculator: Boolean = false,

    // Автообновление
    val autoRefresh: Boolean = true,
    val refreshOnStart: Boolean = true,
    val keepScreenOn: Boolean = false,
    val confirmExit: Boolean = false,

    // Уведомления
    val notifications: Boolean = true,
    val rateAlerts: Boolean = false,
    val dailySummary: Boolean = false,
    val alertThreshold: Float = 1f,

    // Системные
    val darkTheme: String = "system",
    val vibration: Boolean = true,
    val soundEffects: Boolean = false,
    val dataSaver: Boolean = false,
    val language: String = "system",
    val biometricLock: Boolean = false,
    val autoClearCache: Boolean = false,

    // Крипто
    val cryptoBaseCurrency: String = "USD",
    val defaultCryptoSymbol: String = "BTC",
    val showCrypto: Boolean = true
)

// ========== СПИСОК ДОСТУПНЫХ ФИАТНЫХ ВАЛЮТ ==========
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