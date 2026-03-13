package com.kyki.kyki.Cards.Currency

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.kyki.kyki.*

@Composable
fun DetailCard(
    data: CurrencyData,
    settings: AppSettings,
    onOpenCalculator: () -> Unit
) {
    val context = LocalContext.current
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val currencyInfo = AVAILABLE_CURRENCIES.find { it.code == settings.defaultCurrency }
        ?: AVAILABLE_CURRENCIES[0]
    val fontScale = getFontSize(settings.fontSize)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = if (settings.blockShadows)
            CardDefaults.cardElevation(settings.elevationLevel.dp)
        else
            CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding((20 * fontScale).dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)
            ) {
                Box(
                    modifier = Modifier
                        .size((52 * fontScale).dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .then(if (settings.blockShadows) Modifier.shadow(6.dp, CircleShape) else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    Text(currencyInfo.flag, fontSize = (28 * fontScale).sp)
                }

                Column {
                    Text(
                        "${currencyInfo.code}/BYN",
                        color = if (isDark) Color.White else Color(0xFF1C1C1E),
                        fontSize = (22 * fontScale).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        currencyInfo.name,
                        color = Color(0xFF8E8E93),
                        fontSize = (13 * fontScale).sp
                    )
                }
            }

            Spacer(modifier = Modifier.height((20 * fontScale).dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isDark) Color(0xFF0F0F0F) else Color(0xFFF8F9FA))
                    .padding((18 * fontScale).dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "1 ${currencyInfo.code} =",
                        color = Color(0xFF8E8E93),
                        fontSize = (14 * fontScale).sp
                    )
                    Text(
                        formatPrice(data.rate, settings.decimalPlaces),
                        color = if (isDark) Color.White else Color(0xFF1C1C1E),
                        fontSize = (42 * fontScale).sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "BYN",
                        color = primaryColor,
                        fontSize = (17 * fontScale).sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height((16 * fontScale).dp))
            Divider(color = Color(0xFF3A3A3C), thickness = 1.dp)
            Spacer(modifier = Modifier.height((16 * fontScale).dp))

            if (settings.showBuySell) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Buy", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                        Text(
                            formatPrice(data.buyRate, settings.decimalPlaces),
                            color = Color(0xFF34C759),
                            fontSize = (19 * fontScale).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height((44 * fontScale).dp)
                            .background(Color(0xFF3A3A3C))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sell", color = Color(0xFF8E8E93), fontSize = (12 * fontScale).sp)
                        Text(
                            formatPrice(data.sellRate, settings.decimalPlaces),
                            color = Color(0xFFFF3B30),
                            fontSize = (19 * fontScale).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height((12 * fontScale).dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Spread: ${String.format("%.2f", (data.sellRate - data.buyRate) * 100)}%",
                    color = Color(0xFF8E8E93),
                    fontSize = (12 * fontScale).sp
                )
                Text(
                    "Updated: ${data.updateTime}",
                    color = Color(0xFF8E8E93),
                    fontSize = (12 * fontScale).sp
                )
            }

            Spacer(modifier = Modifier.height((20 * fontScale).dp))

            CalculatorButton(settings, primaryColor, fontScale, onOpenCalculator)

            Spacer(modifier = Modifier.height((12 * fontScale).dp))

            NbrbButton(settings, primaryColor, fontScale, context)
        }
    }
}

@Composable
private fun CalculatorButton(
    settings: AppSettings,
    primaryColor: Color,
    fontScale: Float,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val animSpeed = getAnimationDuration(settings.animationSpeed)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = if (settings.buttonAnimations && animSpeed > 0)
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        else
            tween(0)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(14.dp))
            .background(primaryColor.copy(0.2f))
            .clickable { isPressed = true; onClick() }
            .padding(vertical = (12 * fontScale).dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Calculate, null, tint = primaryColor)
            Text(
                "Open Calculator",
                color = primaryColor,
                fontSize = (15 * fontScale).sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(140)
            isPressed = false
        }
    }
}

@Composable
private fun NbrbButton(
    settings: AppSettings,
    primaryColor: Color,
    fontScale: Float,
    context: Context
) {
    var isPressed by remember { mutableStateOf(false) }
    val animSpeed = getAnimationDuration(settings.animationSpeed)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = if (settings.buttonAnimations && animSpeed > 0)
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        else
            tween(0)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(14.dp))
            .background(primaryColor)
            .clickable {
                isPressed = true
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nbrb.by/"))
                )
            }
            .padding(vertical = (15 * fontScale).dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Open NBRB website →",
            color = Color.White,
            fontSize = (15 * fontScale).sp,
            fontWeight = FontWeight.SemiBold
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(140)
            isPressed = false
        }
    }
}