package com.kyki.kyki.Cards.Currency

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyki.kyki.*

@Composable
fun CryptoDetailScreen(
    data: CryptoData,
    info: CryptoInfo,
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
                "Crypto Details",
                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                fontSize = (17 * fontScale).sp,
                fontWeight = FontWeight.SemiBold
            )
            Text("      ", fontSize = (17 * fontScale).sp)
        }

        CryptoDetailCard(
            data = data,
            info = info,
            settings = settings,
            onOpenCalculator = onOpenCalculator
        )
    }
}