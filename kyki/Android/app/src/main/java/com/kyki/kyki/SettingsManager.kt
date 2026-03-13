package com.kyki.kyki

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("kyki_settings_v4", Context.MODE_PRIVATE)
    private val gson = Gson()

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
            autoClearCache = prefs.getBoolean("auto_clear_cache", false),
            cryptoBaseCurrency = prefs.getString("crypto_base_currency", "USD") ?: "USD",
            showCrypto = prefs.getBoolean("show_crypto", true),
            defaultCryptoSymbol = prefs.getString("default_crypto_symbol", "BTC") ?: "BTC"
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
            putString("crypto_base_currency", settings.cryptoBaseCurrency)
            putBoolean("show_crypto", settings.showCrypto)
            putString("default_crypto_symbol", settings.defaultCryptoSymbol)

            apply()
        }
    }
}