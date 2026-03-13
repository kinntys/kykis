package com.kyki.kyki.Cards.Currency

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.round
import com.kyki.kyki.AppSettings
import com.kyki.kyki.CurrencyInfo
import com.kyki.kyki.AVAILABLE_CURRENCIES
import com.kyki.kyki.getPrimaryColor
import com.kyki.kyki.getFontSize
import com.kyki.kyki.fetchSingleRate
import com.kyki.kyki.getAnimationDuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background

@Composable
fun CalculatorScreen(settings: AppSettings, onBack: () -> Unit) {
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
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
    if (settings.roundCalculator) result = round(result)

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
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
                "Calculator",
                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                fontSize = (17 * fontScale).sp,
                fontWeight = FontWeight.SemiBold
            )
            Text("      ", fontSize = (17 * fontScale).sp)
        }

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = if (settings.blockShadows)
                CardDefaults.cardElevation(settings.elevationLevel.dp)
            else
                CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = if (isDark) Color(0xFF1C1C1E) else Color.White
            )
        ) {
            Column(modifier = Modifier.padding((20 * fontScale).dp)) {
                Text(
                    "Amount",
                    color = Color(0xFF8E8E93),
                    fontSize = (14 * fontScale).sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(fontSize = (16 * fontScale).sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor
                    )
                )

                Spacer(modifier = Modifier.height((20 * fontScale).dp))

                Text(
                    "From",
                    color = Color(0xFF8E8E93),
                    fontSize = (14 * fontScale).sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CurrencySelector(
                    selected = fromCurrency,
                    onSelect = { fromCurrency = it },
                    includeBYN = true,
                    settings = settings
                )

                Spacer(modifier = Modifier.height((12 * fontScale).dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            val temp = fromCurrency
                            fromCurrency = toCurrency
                            toCurrency = temp
                        },
                        modifier = Modifier
                            .background(primaryColor.copy(0.1f), CircleShape)
                    ) {
                        Icon(Icons.Default.SwapVert, "Swap", tint = primaryColor)
                    }
                }

                Spacer(modifier = Modifier.height((12 * fontScale).dp))

                Text(
                    "To",
                    color = Color(0xFF8E8E93),
                    fontSize = (14 * fontScale).sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CurrencySelector(
                    selected = toCurrency,
                    onSelect = { toCurrency = it },
                    includeBYN = true,
                    settings = settings
                )

                Spacer(modifier = Modifier.height((24 * fontScale).dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isDark) Color(0xFF0F0F0F) else Color(0xFFF8F9FA))
                        .padding((20 * fontScale).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Result", color = Color(0xFF8E8E93), fontSize = (14 * fontScale).sp)
                        if (isLoading) {
                            CircularProgressIndicator(color = primaryColor)
                        } else {
                            Text(
                                String.format(formatString, result),
                                color = if (isDark) Color.White else Color(0xFF1C1C1E),
                                fontSize = (36 * fontScale).sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                toCurrency,
                                color = primaryColor,
                                fontSize = (18 * fontScale).sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                if (!isLoading) {
                    Spacer(modifier = Modifier.height((12 * fontScale).dp))
                    Text(
                        "1 $fromCurrency = ${String.format(formatString, exchangeRate / fromInfo.scale)} BYN",
                        color = Color(0xFF8E8E93),
                        fontSize = (12 * fontScale).sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun CurrencySelector(
    selected: String,
    onSelect: (String) -> Unit,
    includeBYN: Boolean,
    settings: AppSettings
) {
    val isDark = when (settings.darkTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val primaryColor = getPrimaryColor(settings.primaryColor)
    val fontScale = getFontSize(settings.fontSize)

    val currencies = if (includeBYN)
        listOf(CurrencyInfo("BYN", "Belarusian Ruble", "🇧🇾", 0, 1)) + AVAILABLE_CURRENCIES
    else
        AVAILABLE_CURRENCIES

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, primaryColor.copy(0.5f))
        ) {
            Row(
                modifier = Modifier
                    .padding((16 * fontScale).dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val info = currencies.find { it.code == selected } ?: currencies[0]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)
                ) {
                    Text(info.flag, fontSize = (24 * fontScale).sp)
                    Column {
                        Text(
                            info.code,
                            color = if (isDark) Color.White else Color(0xFF1C1C1E),
                            fontSize = (16 * fontScale).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            info.name,
                            color = Color(0xFF8E8E93),
                            fontSize = (12 * fontScale).sp
                        )
                    }
                }
                Icon(Icons.Default.ArrowDropDown, null, tint = primaryColor)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 400.dp)
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy((12 * fontScale).dp)
                        ) {
                            Text(currency.flag, fontSize = (20 * fontScale).sp)
                            Column {
                                Text(
                                    currency.code,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = (14 * fontScale).sp
                                )
                                Text(
                                    currency.name,
                                    fontSize = (12 * fontScale).sp,
                                    color = Color(0xFF8E8E93)
                                )
                            }
                        }
                    },
                    onClick = {
                        onSelect(currency.code)
                        expanded = false
                    }
                )
            }
        }
    }
}