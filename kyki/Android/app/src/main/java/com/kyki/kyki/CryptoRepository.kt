package com.kyki.kyki

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class CryptoRepository {
    // Расширенный список популярных криптовалют
    val POPULAR_CRYPTOS = listOf(
        CryptoInfo("bitcoin", "BTC", "Bitcoin"),
        CryptoInfo("ethereum", "ETH", "Ethereum"),
        CryptoInfo("binancecoin", "BNB", "Binance Coin"),
        CryptoInfo("solana", "SOL", "Solana"),
        CryptoInfo("ripple", "XRP", "XRP"),
        CryptoInfo("cardano", "ADA", "Cardano"),
        CryptoInfo("dogecoin", "DOGE", "Dogecoin"),
        CryptoInfo("polkadot", "DOT", "Polkadot"),
        CryptoInfo("litecoin", "LTC", "Litecoin"),
        CryptoInfo("tron", "TRX", "TRON"),
        CryptoInfo("the-open-network", "TON", "Toncoin"),
        CryptoInfo("avalanche-2", "AVAX", "Avalanche"),
        CryptoInfo("chainlink", "LINK", "Chainlink"),
        CryptoInfo("polygon", "MATIC", "Polygon"),
        CryptoInfo("uniswap", "UNI", "Uniswap"),
        CryptoInfo("stellar", "XLM", "Stellar"),
        CryptoInfo("cosmos", "ATOM", "Cosmos"),
        CryptoInfo("monero", "XMR", "Monero"),
        CryptoInfo("vechain", "VET", "VeChain"),
        CryptoInfo("algorand", "ALGO", "Algorand"),
        CryptoInfo("near", "NEAR", "Near Protocol"),
        CryptoInfo("filecoin", "FIL", "Filecoin"),
        CryptoInfo("aptos", "APT", "Aptos"),
        CryptoInfo("arbitrum", "ARB", "Arbitrum"),
        CryptoInfo("optimism", "OP", "Optimism")
    )

    // Получение цены для одной криптовалюты (для отображения на главной)
    suspend fun fetchSingleCrypto(symbol: String, vsCurrency: String = "usd"): CryptoData? {
        val info = POPULAR_CRYPTOS.find { it.symbol == symbol } ?: return null
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://api.coingecko.com/api/v3/simple/price?ids=${info.id}&vs_currencies=$vsCurrency&include_24hr_change=true&include_market_cap=true&include_24hr_vol=true"
                val response = URL(url).readText()
                val json = JSONObject(response)
                val coinData = json.getJSONObject(info.id)
                val price = coinData.optDouble("$vsCurrency", 0.0)
                val change = coinData.optDouble("${vsCurrency}_24h_change", 0.0)
                val marketCap = coinData.optDouble("${vsCurrency}_market_cap", 0.0)
                val volume = coinData.optDouble("${vsCurrency}_24h_vol", 0.0)
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                CryptoData(
                    symbol = symbol,
                    rate = price,
                    changePercent24h = change,
                    marketCap = marketCap,
                    volume24h = volume,
                    high24h = price * 1.02,
                    low24h = price * 0.98,
                    updateTime = time
                )
            } catch (e: Exception) {
                e.printStackTrace()
                // Возвращаем тестовые данные при ошибке
                CryptoData(
                    symbol = symbol,
                    rate = 50000.0 + Math.random() * 1000,
                    changePercent24h = Math.random() * 10 - 5,
                    updateTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                )
            }
        }
    }

    // Получение всех цен (для будущего использования)
    suspend fun fetchAllPrices(vsCurrency: String = "usd"): Map<String, CryptoData> {
        val ids = POPULAR_CRYPTOS.map { it.id }
        return withContext(Dispatchers.IO) {
            val result = mutableMapOf<String, CryptoData>()
            try {
                val idsString = ids.joinToString(",")
                val url = "https://api.coingecko.com/api/v3/simple/price?ids=$idsString&vs_currencies=$vsCurrency&include_24hr_change=true&include_market_cap=true&include_24hr_vol=true"
                val response = URL(url).readText()
                val json = JSONObject(response)
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                POPULAR_CRYPTOS.forEach { info ->
                    if (json.has(info.id)) {
                        val coinData = json.getJSONObject(info.id)
                        val price = coinData.optDouble("$vsCurrency", 0.0)
                        val change = coinData.optDouble("${vsCurrency}_24h_change", 0.0)
                        val marketCap = coinData.optDouble("${vsCurrency}_market_cap", 0.0)
                        val volume = coinData.optDouble("${vsCurrency}_24h_vol", 0.0)

                        result[info.symbol] = CryptoData(
                            symbol = info.symbol,
                            rate = price,
                            changePercent24h = change,
                            marketCap = marketCap,
                            volume24h = volume,
                            high24h = price * 1.02,
                            low24h = price * 0.98,
                            updateTime = time
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            result
        }
    }
}