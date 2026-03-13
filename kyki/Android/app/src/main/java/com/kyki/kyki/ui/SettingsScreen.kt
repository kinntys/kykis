package com.kyki.kyki.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import com.kyki.kyki.Cards.Currency.CurrencyCard
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyki.kyki.AppSettings
import com.kyki.kyki.CurrencyData
import com.kyki.kyki.getFontSize
import com.kyki.kyki.getPrimaryColor
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onBack: () -> Unit
) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    var currentScreen by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize()) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    null,
                    tint = primaryColor,
                    modifier = Modifier
                        .clickable { onBack() }
                        .size(28.dp)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    "Настройки",
                    fontSize = 22.sp * fontScale,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        when (currentScreen) {
            "customization" -> CustomizationScreen(settings, onSettingsChange) { currentScreen = null }
            "functional" -> FunctionalScreen(settings, onSettingsChange) { currentScreen = null }
            "system" -> SystemScreen(settings, onSettingsChange) { currentScreen = null }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    item {
                        SettingsCategoryItem(
                            Icons.Default.Palette,
                            "Кастомизация",
                            "Цвета, карточки, анимации, фон",
                            Color(0xFF5856D6),
                            { currentScreen = "customization" },
                            settings
                        )
                    }
                    item { Spacer(Modifier.height(12.dp)) }
                    item {
                        SettingsCategoryItem(
                            Icons.Default.Tune,
                            "Функционал",
                            "Данные, калькулятор, уведомления",
                            Color(0xFF34C759),
                            { currentScreen = "functional" },
                            settings
                        )
                    }
                    item { Spacer(Modifier.height(12.dp)) }
                    item {
                        SettingsCategoryItem(
                            Icons.Default.Settings,
                            "Система",
                            "Тема, безопасность, приватность",
                            Color(0xFFFF9500),
                            { currentScreen = "system" },
                            settings
                        )
                    }

                    item { Spacer(Modifier.height(32.dp)) }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFF3B30))
                                .clickable { onSettingsChange(AppSettings()) }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Сбросить все настройки",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCategoryItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    settings: AppSettings
) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val fontScale = getFontSize(settings.fontSize)

    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable { pressed = true; onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color.copy(0.15f)),
                contentAlignment = Alignment.Center
            ) {
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
    LaunchedEffect(pressed) {
        if (pressed) {
            delay(120)
            pressed = false
        }
    }
}

@Composable
fun CustomizationScreen(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onBack: () -> Unit
) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val primary = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item { SettingsScreenHeader("Кастомизация", onBack, primary) }

        item { SettingsSection("Цвета и фон", isDark, fontScale) }
        item { ColorPickerRow(settings.primaryColor) { onSettingsChange(settings.copy(primaryColor = it)) } }
        item { BackgroundPatternPicker(settings.backgroundPattern) { onSettingsChange(settings.copy(backgroundPattern = it)) } }

        item { SettingsSection("Карточки", isDark, fontScale) }
        item { CardStylePicker(settings.cardStyle) { onSettingsChange(settings.copy(cardStyle = it)) } }
        item {
            ToggleSettingItem(
                "Компактные карточки",
                settings.compactCards,
                isDark,
                primary,
                fontScale
            ) { onSettingsChange(settings.copy(compactCards = it)) }
        }

        item {
            ToggleSettingItem(
                "Показывать флаги",
                settings.showFlags,
                isDark,
                primary,
                fontScale
            ) { onSettingsChange(settings.copy(showFlags = it)) }
        }
        item {
            ToggleSettingItem(
                "Показывать график истории",
                settings.showHistoryChart,
                isDark,
                primary,
                fontScale
            ) { onSettingsChange(settings.copy(showHistoryChart = it)) }
        }

        item { SettingsSection("Live Preview (меняй настройки выше)", isDark, fontScale) }
        item {
            val sample = remember {
                CurrencyData(
                    3.2850, 3.27, 3.23, 3.34,
                    "14:30",
                    listOf(3.20, 3.22, 3.25, 3.24, 3.26, 3.28, 3.2850),
                    0.46
                )
            }
            CurrencyCard(sample, settings, {})        }

        item { Spacer(Modifier.height(100.dp)) }
    }
}

@Composable
fun FunctionalScreen(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onBack: () -> Unit
) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item { SettingsScreenHeader("Функционал", onBack, primaryColor) }

        item { SettingsSection("Данные", isDark, fontScale) }
        item {
            ToggleSettingItem(
                "Автообновление",
                settings.autoRefresh,
                isDark,
                primaryColor,
                fontScale
            ) { onSettingsChange(settings.copy(autoRefresh = it)) }
        }
        item {
            SliderSettingItem(
                "Интервал обновления (мин)",
                settings.refreshInterval.toFloat(),
                5f,
                120f,
                isDark,
                primaryColor,
                fontScale,
                { "${it.toInt()} мин" }
            ) { onSettingsChange(settings.copy(refreshInterval = it.toInt())) }
        }

        item { SettingsSection("Калькулятор", isDark, fontScale) }
        item {
            SliderSettingItem(
                "Кол-во знаков",
                settings.decimalPlaces.toFloat(),
                0f,
                6f,
                isDark,
                primaryColor,
                fontScale,
                { it.toInt().toString() }
            ) { onSettingsChange(settings.copy(decimalPlaces = it.toInt())) }
        }
        item {
            ToggleSettingItem(
                "Округлять результат",
                settings.roundCalculator,
                isDark,
                primaryColor,
                fontScale
            ) { onSettingsChange(settings.copy(roundCalculator = it)) }
        }

        item { SettingsSection("Уведомления", isDark, fontScale) }
        item {
            ToggleSettingItem(
                "Включить уведомления",
                settings.notifications,
                isDark,
                primaryColor,
                fontScale
            ) { onSettingsChange(settings.copy(notifications = it)) }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun SystemScreen(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onBack: () -> Unit
) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item { SettingsScreenHeader("Система", onBack, primaryColor) }

        item { SettingsSection("Тема", isDark, fontScale) }
        item {
            val options = listOf("system" to "Системная", "light" to "Светлая", "dark" to "Тёмная")
            val selectedText = options.find { it.first == settings.darkTheme }?.second ?: "Системная"
            SegmentedControl(
                options.map { it.second },
                selectedText
            ) { newText ->
                val newValue = options.find { it.second == newText }?.first ?: "system"
                onSettingsChange(settings.copy(darkTheme = newValue))
            }
        }

        item { SettingsSection("Безопасность", isDark, fontScale) }
        item {
            ToggleSettingItem(
                "Блокировка по биометрии",
                settings.biometricLock,
                isDark,
                primaryColor,
                fontScale
            ) { onSettingsChange(settings.copy(biometricLock = it)) }
        }

        item { SettingsSection("Приватность", isDark, fontScale) }
        item {
            ToggleSettingItem(
                "Режим экономии данных",
                settings.dataSaver,
                isDark,
                primaryColor,
                fontScale
            ) { onSettingsChange(settings.copy(dataSaver = it)) }
        }
        item {
            ToggleSettingItem(
                "Автоочистка кэша",
                settings.autoClearCache,
                isDark,
                primaryColor,
                fontScale
            ) { onSettingsChange(settings.copy(autoClearCache = it)) }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun SettingsScreenHeader(title: String, onBack: () -> Unit, primary: Color) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "← Назад",
            color = primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onBack() }
        )
        Spacer(Modifier.weight(1f))
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SettingsSection(title: String, isDark: Boolean, fontScale: Float) {
    Text(
        title.uppercase(),
        color = if (isDark) Color(0xFF8E8E93) else Color(0xFF636366),
        fontSize = (13 * fontScale).sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = (12 * fontScale).dp, top = (16 * fontScale).dp)
    )
}

@Composable
fun ColorPickerRow(selected: String, onSelect: (String) -> Unit) {
    val colors = listOf(
        "blue", "green", "purple", "orange", "red",
        "pink", "teal", "indigo", "yellow", "mint", "brown"
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        colors.forEach { color ->
            val colorValue = getPrimaryColor(color)
            val isSelected = color == selected
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colorValue)
                    .border(
                        width = if (isSelected) 3.dp else 0.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clickable { onSelect(color) }
            )
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
fun CardStylePicker(current: String, onChange: (String) -> Unit) {
    val options = listOf("elevated", "filled", "outlined", "flat")
    val selectedText = current.replaceFirstChar { it.uppercase() }
    SegmentedControl(listOf("Elevated", "Filled", "Outlined", "Flat"), selectedText) { newText ->
        onChange(newText.lowercase())
    }
}

@Composable
fun ToggleSettingItem(
    title: String,
    checked: Boolean,
    isDark: Boolean,
    activeColor: Color,
    fontScale: Float,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = (4 * fontScale).dp)
            .clickable { onCheckedChange(!checked) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            color = if (isDark) Color.White else Color(0xFF1C1C1E),
            fontSize = (16 * fontScale).sp
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = activeColor,
                checkedTrackColor = activeColor.copy(alpha = 0.5f)
            )
        )
    }
    Divider(
        color = if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA),
        thickness = 0.5.dp
    )
}

@Composable
fun SliderSettingItem(
    title: String,
    value: Float,
    min: Float,
    max: Float,
    isDark: Boolean,
    activeColor: Color,
    fontScale: Float,
    valueFormatter: (Float) -> String,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = (8 * fontScale).dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                fontSize = (16 * fontScale).sp
            )
            Text(
                valueFormatter(value),
                color = activeColor,
                fontSize = (14 * fontScale).sp,
                fontWeight = FontWeight.Medium
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = min..max,
            colors = SliderDefaults.colors(
                thumbColor = activeColor,
                activeTrackColor = activeColor,
                inactiveTrackColor = if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA)
            )
        )
    }
}

@Composable
fun SegmentedControl(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF2C2C2E).copy(alpha = 0.5f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color(0xFF3A3A3C) else Color.Transparent)
                    .clickable { onSelect(option) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    option,
                    color = if (isSelected) getPrimaryColor("blue") else Color(0xFF8E8E93),
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}