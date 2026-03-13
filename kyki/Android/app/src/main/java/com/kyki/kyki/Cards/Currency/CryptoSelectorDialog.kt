package com.kyki.kyki.Cards.Currency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyki.kyki.*

@Composable
fun CryptoSelectorDialog(
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
    val repository = remember { CryptoRepository() }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White,
        title = {
            Text(
                "Выберите криптовалюту",
                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(repository.POPULAR_CRYPTOS) { crypto ->
                    val isSelected = crypto.symbol == selected
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) primaryColor.copy(0.1f) else Color.Transparent)
                            .clickable { onSelect(crypto.symbol) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(getCryptoEmoji(crypto.symbol), fontSize = 24.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                crypto.symbol,
                                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                crypto.name,
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