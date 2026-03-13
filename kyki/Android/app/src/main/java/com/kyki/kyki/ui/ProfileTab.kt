package com.kyki.kyki.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.alpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.kyki.kyki.AppSettings
import com.kyki.kyki.AVAILABLE_CURRENCIES
import com.kyki.kyki.getPrimaryColor
import com.kyki.kyki.getFontSize
import com.kyki.kyki.getAnimationDuration

@Composable
fun ProfileTab(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit
) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val scrollState = rememberScrollState()
    val animDuration = getAnimationDuration(settings.animationSpeed)
    val appearAnimation = remember { Animatable(0f) }
    val fontScale = getFontSize(settings.fontSize)

    var showSettings by remember { mutableStateOf(false) }

    if (settings.listAnimations && animDuration > 0) {
        LaunchedEffect(Unit) {
            appearAnimation.animateTo(1f, tween(animDuration, easing = EaseOutQuart))
        }
    } else {
        LaunchedEffect(Unit) { appearAnimation.snapTo(1f) }
    }

    if (showSettings) {
        SettingsScreen(settings, onSettingsChange) { showSettings = false }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .alpha(appearAnimation.value)
            .verticalScroll(scrollState)
    ) {
        // Profile Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = if (settings.blockShadows)
                CardDefaults.cardElevation(settings.elevationLevel.dp)
            else
                CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding((20 * fontScale).dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy((14 * fontScale).dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size((56 * fontScale).dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(primaryColor, Color(0xFF5856D6))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "U",
                                color = Color.White,
                                fontSize = (24 * fontScale).sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            Text(
                                "User",
                                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                                fontSize = (20 * fontScale).sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "@username",
                                color = Color(0xFF8E8E93),
                                fontSize = (14 * fontScale).sp
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDark) Color(0xFF2C2C2E) else Color(0xFFF0F0F0))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "PRO",
                            color = primaryColor,
                            fontSize = (12 * fontScale).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height((16 * fontScale).dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
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

        // Settings Button
        var isPressed by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            if (isPressed) 0.95f else 1f,
            if (settings.buttonAnimations) spring(dampingRatio = Spring.DampingRatioMediumBouncy) else tween(0)
        )

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .clickable { isPressed = true; showSettings = true },
            shape = RoundedCornerShape(16.dp),
            elevation = if (settings.blockShadows)
                CardDefaults.cardElevation(6.dp)
            else
                CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .padding((16 * fontScale).dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)
                ) {
                    Icon(
                        Icons.Default.Settings,
                        null,
                        tint = primaryColor,
                        modifier = Modifier.size((24 * fontScale).dp)
                    )
                    Column {
                        Text(
                            "Settings",
                            color = if (isDark) Color.White else Color(0xFF1C1C1E),
                            fontSize = (16 * fontScale).sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Customize app appearance",
                            color = Color(0xFF8E8E93),
                            fontSize = (12 * fontScale).sp
                        )
                    }
                }
                Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF8E8E93))
            }
        }
        LaunchedEffect(isPressed) {
            if (isPressed) {
                delay(150)
                isPressed = false
            }
        }

        Spacer(modifier = Modifier.height((12 * fontScale).dp))

        // Quick Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)
        ) {
            QuickActionButton(
                Icons.Default.Notifications,
                "Alerts",
                isDark,
                primaryColor,
                Modifier.weight(1f),
                settings
            )
            QuickActionButton(
                Icons.Default.Share,
                "Share",
                isDark,
                primaryColor,
                Modifier.weight(1f),
                settings
            )
        }

        Spacer(modifier = Modifier.height((12 * fontScale).dp))

        // Info Cards
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = if (settings.blockShadows)
                CardDefaults.cardElevation(6.dp)
            else
                CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding((16 * fontScale).dp)) {
                InfoRow(
                    Icons.Default.Info,
                    "App version 3.0.0",
                    isDark,
                    primaryColor,
                    fontScale
                )
                Divider(
                    modifier = Modifier.padding(vertical = (12 * fontScale).dp),
                    color = if (isDark) Color(0xFF3A3A3C) else Color(0xFFE5E5EA)
                )
                InfoRow(
                    Icons.Default.Star,
                    "Rate app",
                    isDark,
                    primaryColor,
                    fontScale
                )
            }
        }

        Spacer(modifier = Modifier.height((12 * fontScale).dp))

        // About Card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = if (settings.blockShadows)
                CardDefaults.cardElevation(4.dp)
            else
                CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding((16 * fontScale).dp)) {
                Text(
                    "About",
                    color = if (isDark) Color.White else Color(0xFF1C1C1E),
                    fontSize = (16 * fontScale).sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height((8 * fontScale).dp))
                Text(
                    "Track ${AVAILABLE_CURRENCIES.size}+ exchange rates from NBRB with advanced customization options.",
                    color = Color(0xFF8E8E93),
                    fontSize = (14 * fontScale).sp,
                    lineHeight = (20 * fontScale).sp
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun StatItem(value: String, label: String, isDark: Boolean, fontScale: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            color = if (isDark) Color.White else Color(0xFF1C1C1E),
            fontSize = (20 * fontScale).sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            color = Color(0xFF8E8E93),
            fontSize = (12 * fontScale).sp
        )
    }
}

@Composable
fun StatusChip(
    text: String,
    isActive: Boolean,
    isDark: Boolean,
    primaryColor: Color,
    fontScale: Float
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isActive) primaryColor
                else (if (isDark) Color(0xFF2C2C2E) else Color(0xFFF0F0F0))
            )
            .padding(horizontal = (14 * fontScale).dp, vertical = (6 * fontScale).dp)
    ) {
        Text(
            text,
            color = if (isActive) Color.White else Color(0xFF8E8E93),
            fontSize = (13 * fontScale).sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    isDark: Boolean,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    settings: AppSettings
) {
    val fontScale = getFontSize(settings.fontSize)
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        if (isPressed) 0.95f else 1f,
        if (settings.buttonAnimations) spring(dampingRatio = Spring.DampingRatioMediumBouncy) else tween(0)
    )

    ElevatedCard(
        modifier = modifier
            .scale(scale)
            .clickable { isPressed = true },
        shape = RoundedCornerShape(14.dp),
        elevation = if (settings.blockShadows)
            CardDefaults.cardElevation(4.dp)
        else
            CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = (16 * fontScale).dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                null,
                tint = primaryColor,
                modifier = Modifier.size((24 * fontScale).dp)
            )
            Spacer(modifier = Modifier.height((6 * fontScale).dp))
            Text(
                label,
                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                fontSize = (13 * fontScale).sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    text: String,
    isDark: Boolean,
    primaryColor: Color,
    fontScale: Float
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)
    ) {
        Icon(
            icon,
            null,
            tint = primaryColor,
            modifier = Modifier.size((20 * fontScale).dp)
        )
        Text(
            text,
            color = if (isDark) Color.White else Color(0xFF1C1C1E),
            fontSize = (15 * fontScale).sp
        )
    }
}