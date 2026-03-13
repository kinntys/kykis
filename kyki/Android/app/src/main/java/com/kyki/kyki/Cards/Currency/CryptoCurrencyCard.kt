package com.kyki.kyki.Cards.Currency

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.kyki.kyki.*
import androidx.compose.foundation.BorderStroke

@Composable
fun CryptoCurrencyCard(
    data: CryptoData,
    info: CryptoInfo,
    settings: AppSettings,
    onClick: () -> Unit,
    onCryptoSelect: (() -> Unit)? = null
) {
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val radius = getCornerRadius(settings.cornerRadius)
    val change = data.changePercent24h
    val isPositive = change >= 0
    val changeColor = if (isPositive) Color(0xFF34C759) else Color(0xFFFF3B30)
    val sign = if (isPositive) "+" else ""
    val baseCurrency = settings.cryptoBaseCurrency

    var isPressed by remember { mutableStateOf(false) }
    val animSpeed = getAnimationDuration(settings.animationSpeed)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = if (settings.buttonAnimations && animSpeed > 0)
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        else
            tween(0)
    )

    val cardModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = getCardSpacing(settings.cardSpacing) / 2)
        .scale(scale)
        .clickable { isPressed = true; onClick() }
        .alpha(settings.blockTransparency)

    val cardContent = @Composable {
        CryptoCardContent(
            data = data,
            info = info,
            settings = settings,
            primaryColor = primaryColor,
            isDark = isDark,
            changeColor = changeColor,
            sign = sign,
            baseCurrency = baseCurrency,
            onCryptoSelect = onCryptoSelect
        )
    }

    when (settings.cardStyle) {
        "filled" -> Card(
            modifier = cardModifier,
            shape = RoundedCornerShape(radius),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF2C2C2E) else Color(0xFFF0F0F0)
            )
        ) { cardContent() }

        "outlined" -> OutlinedCard(
            modifier = cardModifier,
            shape = RoundedCornerShape(radius),
            border = BorderStroke(
                width = if (settings.blockBorders) 2.dp else 1.dp,
                color = if (settings.blockBorders) primaryColor else primaryColor.copy(0.5f)
            )
        ) { cardContent() }

        "flat" -> Card(
            modifier = cardModifier,
            shape = RoundedCornerShape(radius),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) { cardContent() }

        else -> ElevatedCard(
            modifier = cardModifier,
            shape = RoundedCornerShape(radius),
            colors = CardDefaults.elevatedCardColors(
                containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White
            ),
            elevation = if (settings.blockShadows)
                CardDefaults.cardElevation(settings.elevationLevel.dp)
            else
                CardDefaults.cardElevation(0.dp)
        ) { cardContent() }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun CryptoCardContent(
    data: CryptoData,
    info: CryptoInfo,
    settings: AppSettings,
    primaryColor: Color,
    isDark: Boolean,
    changeColor: Color,
    sign: String,
    baseCurrency: String,
    onCryptoSelect: (() -> Unit)?
) {
    val fontScale = getFontSize(settings.fontSize)
    val cardPadding = if (settings.compactCards)
        (8 * fontScale).dp
    else
        (16 * fontScale).dp

    Column(modifier = Modifier.padding(cardPadding)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((8 * fontScale).dp)
            ) {
                Box(
                    modifier = Modifier
                        .size((44 * fontScale).dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0xFF2C2C2E) else Color(0xFFE8E8E8))
                        .then(if (settings.blockShadows) Modifier.shadow(4.dp, CircleShape) else Modifier)
                        .then(
                            if (onCryptoSelect != null)
                                Modifier.clickable { onCryptoSelect() }
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        getCryptoEmoji(info.symbol),
                        fontSize = (24 * fontScale).sp
                    )
                }

                if (onCryptoSelect != null) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Выбрать криптовалюту",
                        tint = primaryColor,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onCryptoSelect() }
                    )
                }

                Column {
                    Text(
                        "${info.symbol}/${baseCurrency}",
                        color = if (isDark) Color.White else Color(0xFF1C1C1E),
                        fontSize = (18 * fontScale).sp,
                        fontWeight = if (settings.fontWeight == "bold")
                            FontWeight.Bold
                        else
                            FontWeight.SemiBold
                    )
                    Text(
                        info.name,
                        color = Color(0xFF8E8E93),
                        fontSize = (12 * fontScale).sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    formatPrice(data.rate, settings.decimalPlaces),
                    color = if (isDark) Color.White else Color(0xFF1C1C1E),
                    fontSize = (28 * fontScale).sp,
                    fontWeight = FontWeight.Bold
                )
                if (settings.showChangePercent) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(changeColor.copy(0.15f))
                            .padding(horizontal = (8 * fontScale).dp, vertical = (3 * fontScale).dp)
                    ) {
                        Text(
                            "$sign${String.format("%.2f", data.changePercent24h)}%",
                            color = changeColor,
                            fontSize = (13 * fontScale).sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        if (settings.showBuySell) {
            Spacer(modifier = Modifier.height((14 * fontScale).dp))
            Divider(
                color = if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height((12 * fontScale).dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("24h High", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                    Text(
                        formatPrice(data.high24h ?: data.rate * 1.02, settings.decimalPlaces),
                        color = Color(0xFF34C759),
                        fontSize = (17 * fontScale).sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height((36 * fontScale).dp)
                        .background(if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA))
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("24h Low", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                    Text(
                        formatPrice(data.low24h ?: data.rate * 0.98, settings.decimalPlaces),
                        color = Color(0xFFFF3B30),
                        fontSize = (17 * fontScale).sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height((36 * fontScale).dp)
                        .background(if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA))
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Volume", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                    Text(
                        formatVolume(data.volume24h),
                        color = primaryColor,
                        fontSize = (17 * fontScale).sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

fun getCryptoEmoji(symbol: String): String = when (symbol.uppercase()) {
    "BTC" -> "₿"
    "ETH" -> "Ξ"
    "BNB" -> "ⓑ"
    "SOL" -> "◎"
    "XRP" -> "✕"
    "ADA" -> "₳"
    "DOGE" -> "Ð"
    "DOT" -> "●"
    "LTC" -> "Ł"
    "TON" -> "⚡"
    "AVAX" -> "🔥"
    "LINK" -> "🔗"
    "MATIC" -> "⬡"
    "UNI" -> "🦄"
    "XLM" -> "★"
    "ATOM" -> "⚛️"
    "XMR" -> "⌘"
    "VET" -> "🌿"
    "ALGO" -> "🌀"
    "NEAR" -> "🔺"
    "FIL" -> "📁"
    "APT" -> "⏺️"
    "ARB" -> "🔷"
    "OP" -> "⛓️"
    else -> "🪙"
}

fun formatVolume(volume: Double?): String {
    if (volume == null || volume == 0.0) return "—"
    return when {
        volume > 1_000_000_000 -> "${(volume / 1_000_000_000).toInt()}B"
        volume > 1_000_000 -> "${(volume / 1_000_000).toInt()}M"
        volume > 1_000 -> "${(volume / 1_000).toInt()}K"
        else -> volume.toInt().toString()
    }
}