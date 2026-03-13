package com.kyki.kyki

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.kyki.kyki.ui.ProfileTab
import com.kyki.kyki.ui.SettingsScreen
import com.kyki.kyki.Cards.Currency.*

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

@Composable
fun AppTheme(settings: AppSettings, content: @Composable () -> Unit) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val primary = getPrimaryColor(settings.primaryColor)

    MaterialTheme(
        colorScheme = if (isDark)
            darkColorScheme(
                background = Color(0xFF0A0A0A),
                surface = Color(0xFF1C1C1E),
                primary = primary
            )
        else
            lightColorScheme(
                background = Color(0xFFF5F7FA),
                surface = Color(0xFFFFFFFF),
                primary = primary
            )
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

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
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val fontScale = getFontSize(settings.fontSize)

    if (settings.pageTransitions && animDuration > 0) {
        LaunchedEffect(Unit) {
            appearAnimation.animateTo(1f, tween(animDuration, easing = EaseOutQuart))
            scaleAnimation.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
        }
    } else {
        LaunchedEffect(Unit) {
            appearAnimation.snapTo(1f)
            scaleAnimation.snapTo(1f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .alpha(appearAnimation.value)
            .scale(scaleAnimation.value)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "← Back",
                color = primaryColor,
                fontSize = (17 * fontScale).sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onBack() }
            )
            Text(
                "Details",
                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                fontSize = (17 * fontScale).sp,
                fontWeight = FontWeight.SemiBold
            )
            Text("      ", fontSize = (17 * fontScale).sp)
        }

        DetailCard(
            data = data,
            settings = settings,
            onOpenCalculator = onOpenCalculator
        )
    }
}

@Composable
fun AppWithRealData(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit
) {
    var showDetail by remember { mutableStateOf(false) }
    var showCryptoDetail by remember { mutableStateOf(false) }
    var showCalculator by remember { mutableStateOf(false) }
    var currency by remember { mutableStateOf<CurrencyData?>(null) }
    var cryptoData by remember { mutableStateOf<CryptoData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var cryptoLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val cryptoRepo = remember { CryptoRepository() }
    var selectedCryptoInfo by remember { mutableStateOf<CryptoInfo?>(null) }

    val hasVibratePermission = remember {
        context.checkSelfPermission(android.Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED
    }

    // Загрузка фиатных данных
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

    // Загрузка крипто данных
    LaunchedEffect(settings.autoRefresh, settings.defaultCryptoSymbol, settings.cryptoBaseCurrency) {
        while (true) {
            if (settings.autoRefresh) {
                cryptoLoading = true
                val vsCurrency = if (settings.cryptoBaseCurrency == "BYN") "usd" else "usd"
                cryptoData = cryptoRepo.fetchSingleCrypto(settings.defaultCryptoSymbol, vsCurrency)
                if (settings.cryptoBaseCurrency == "BYN" && cryptoData != null) {
                    val usdRate = fetchSingleRate("USD")
                    cryptoData = cryptoData?.copy(rate = cryptoData!!.rate * usdRate)
                }
                cryptoLoading = false
                delay(60000)
            } else {
                delay(60000)
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
                showCryptoDetail && selectedCryptoInfo != null && cryptoData != null -> CryptoDetailScreen(
                    data = cryptoData!!,
                    info = selectedCryptoInfo!!,
                    settings = settings,
                    onBack = { showCryptoDetail = false; selectedCryptoInfo = null },
                    onOpenCalculator = { showCalculator = true }
                )
                else -> MainScreen(
                    currency = currency,
                    cryptoData = cryptoData,
                    isLoading = isLoading,
                    cryptoLoading = cryptoLoading,
                    settings = settings,
                    onSettingsChange = onSettingsChange,
                    onOpenDetail = { showDetail = true },
                    onOpenCryptoDetail = { info ->
                        selectedCryptoInfo = info
                        showCryptoDetail = true
                    }
                )
            }
        }
    }
}

@Composable
fun AppBackground(settings: AppSettings) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    when (settings.backgroundPattern) {
        "dots", "lines", "grid" -> Box(Modifier.fillMaxSize().background(if (isDark) Color(0xFF111111) else Color(0xFFF0F0F0)))
        else -> Box(Modifier.fillMaxSize().background(
            if (isDark) Brush.verticalGradient(listOf(Color(0xFF0A0A0A), Color(0xFF1C1C1E)))
            else Brush.verticalGradient(listOf(Color(0xFFF5F7FA), Color(0xFFE8F4F8)))
        ))
    }
}

@Composable
fun MainScreen(
    currency: CurrencyData?,
    cryptoData: CryptoData?,
    isLoading: Boolean,
    cryptoLoading: Boolean,
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    onOpenDetail: () -> Unit,
    onOpenCryptoDetail: (CryptoInfo) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val coroutineScope = rememberCoroutineScope()
    var showCurrencySelector by remember { mutableStateOf(false) }
    var showCryptoSelector by remember { mutableStateOf(false) }
    val cryptoRepo = remember { CryptoRepository() }
    val selectedCryptoInfo = cryptoRepo.POPULAR_CRYPTOS.find { it.symbol == settings.defaultCryptoSymbol }

    Column(modifier = Modifier.fillMaxSize()) {
        Header(settings)
        Spacer(modifier = Modifier.height(getCardSpacing(settings.cardSpacing)))

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            when (page) {
                0 -> AllTab(
                    currency = currency,
                    cryptoData = cryptoData,
                    cryptoInfo = selectedCryptoInfo,
                    isLoading = isLoading,
                    cryptoLoading = cryptoLoading,
                    settings = settings,
                    onOpenDetail = onOpenDetail,
                    onCurrencySelect = { showCurrencySelector = true },
                    onOpenCryptoDetail = { onOpenCryptoDetail(it) },
                    onCryptoSelect = { showCryptoSelector = true }
                )
                1 -> ProfileTab(settings, onSettingsChange)
            }
        }

        EnhancedBottomBar(
            selectedTab = pagerState.currentPage,
            onTabSelected = { coroutineScope.launch { pagerState.animateScrollToPage(it) } },
            settings = settings,
            tabs = listOf("Курсы", "Профиль")
        )
    }

    if (showCurrencySelector) {
        CurrencySelectorDialog(
            selected = settings.defaultCurrency,
            onSelect = { newCurrency ->
                onSettingsChange(settings.copy(defaultCurrency = newCurrency))
                showCurrencySelector = false
            },
            onDismiss = { showCurrencySelector = false },
            settings = settings
        )
    }

    if (showCryptoSelector) {
        CryptoSelectorDialog(
            selected = settings.defaultCryptoSymbol,
            onSelect = { newSymbol ->
                onSettingsChange(settings.copy(defaultCryptoSymbol = newSymbol))
                showCryptoSelector = false
            },
            onDismiss = { showCryptoSelector = false },
            settings = settings
        )
    }
}

@Composable
fun Header(settings: AppSettings) {
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val currencyInfo = AVAILABLE_CURRENCIES.find { it.code == settings.defaultCurrency }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                    animationSpec = infiniteRepeatable(
                        animation = tween(900, easing = EaseInOutSine),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(primaryColor.copy(alpha = pulse))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                )
            }
        }

    }
}

@Composable
fun AllTab(
    currency: CurrencyData?,
    cryptoData: CryptoData?,
    cryptoInfo: CryptoInfo?,
    isLoading: Boolean,
    cryptoLoading: Boolean,
    settings: AppSettings,
    onOpenDetail: () -> Unit,
    onCurrencySelect: () -> Unit,
    onOpenCryptoDetail: (CryptoInfo) -> Unit,
    onCryptoSelect: () -> Unit
) {
    val animDuration = getAnimationDuration(settings.animationSpeed)
    val appearAnimation = remember { Animatable(0f) }

    if (settings.listAnimations && animDuration > 0) {
        LaunchedEffect(Unit) {
            appearAnimation.animateTo(1f, tween(animDuration, easing = EaseOutQuart))
        }
    } else {
        LaunchedEffect(Unit) { appearAnimation.snapTo(1f) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .alpha(appearAnimation.value),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Фиатная карточка
        if (isLoading) {
            LoadingCard(settings)
        } else {
            currency?.let {
                CurrencyCard(
                    data = it,
                    settings = settings,
                    onClick = onOpenDetail,
                    onCurrencySelect = onCurrencySelect
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Крипто карточка
        if (cryptoLoading) {
            LoadingCard(settings)
        } else {
            if (cryptoData != null && cryptoInfo != null) {
                CryptoCurrencyCard(
                    data = cryptoData,
                    info = cryptoInfo,
                    settings = settings,
                    onClick = { onOpenCryptoDetail(cryptoInfo) },
                    onCryptoSelect = onCryptoSelect
                )
            } else {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.Transparent))
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun EnhancedBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    settings: AppSettings,
    tabs: List<String>
) {
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    val icons = listOf(
        Icons.Outlined.MonetizationOn to Icons.Filled.MonetizationOn,
        Icons.Outlined.Person to Icons.Filled.Person
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(30.dp),
        tonalElevation = if (settings.blockShadows) 8.dp else 0.dp,
        shadowElevation = if (settings.blockShadows) 8.dp else 0.dp,
        color = if (isDark) Color(0xFF1C1C1E).copy(0.95f) else Color.White.copy(0.95f)
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEachIndexed { index, label ->
                val isSelected = index == selectedTab
                val (iconOutlined, iconFilled) = icons[index]
                val contentColor = if (isSelected) primaryColor else Color(0xFF8E8E93)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (isSelected) primaryColor.copy(0.15f) else Color.Transparent)
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val icon = if (isSelected) iconFilled else iconOutlined
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = contentColor,
                            modifier = Modifier.size(24.dp)
                        )

                        AnimatedVisibility(
                            visible = isSelected,
                            enter = fadeIn() + expandHorizontally(),
                            exit = fadeOut() + shrinkHorizontally()
                        ) {
                            Text(
                                text = label,
                                color = contentColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Функция для диалога выбора фиатной валюты (должна быть, если её нет)
@Composable
fun CurrencySelectorDialog(
    selected: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
    settings: AppSettings
) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val primaryColor = getPrimaryColor(settings.primaryColor)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White,
        title = {
            Text(
                "Выберите валюту",
                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(AVAILABLE_CURRENCIES) { currency ->
                    val isSelected = currency.code == selected
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) primaryColor.copy(0.1f) else Color.Transparent)
                            .clickable { onSelect(currency.code) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(currency.flag, fontSize = 24.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                currency.code,
                                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                currency.name,
                                color = Color(0xFF8E8E93),
                                fontSize = 12.sp
                            )
                        }
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = primaryColor
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = primaryColor)
            }
        }
    )
}