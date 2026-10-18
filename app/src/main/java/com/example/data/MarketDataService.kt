package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.util.concurrent.TimeUnit

object MarketDataService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .build()

    // Map SignalVerse symbol to Binance Ticker Pair
    private val cryptoPairMap = mapOf(
        "BTCUSD — Bitcoin" to "BTCUSDT",
        "ETHUSD — Ethereum" to "ETHUSDT",
        "SOLUSD — Solana" to "SOLUSDT",
        "XRPUSD — XRP" to "XRPUSDT",
        "LTCUSD — Litecoin" to "LTCUSDT",
        "ADAUSD — Cardano" to "ADAUSDT",
        "DOGEUSD — Dogecoin" to "DOGEUSDT",
        "BNBUSD — BNB" to "BNBUSDT"
    )

    suspend fun fetchLiveCryptoPrices(): Map<String, Double> = withContext(Dispatchers.IO) {
        val prices = mutableMapOf<String, Double>()
        try {
            val symbolsJson = "[\"BTCUSDT\",\"ETHUSDT\",\"SOLUSDT\",\"XRPUSDT\",\"LTCUSDT\",\"ADAUSDT\",\"DOGEUSDT\",\"BNBUSDT\"]"
            val url = "https://api.binance.com/api/v3/ticker/price?symbols=${symbolsJson}"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "SignalVerse-Android/1.0")
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@use
                    val array = JSONArray(body)
                    val rawMap = mutableMapOf<String, Double>()
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val pair = obj.getString("symbol")
                        val price = obj.getString("price").toDoubleOrNull()
                        if (price != null) {
                            rawMap[pair] = price
                        }
                    }

                    // Map back to SignalVerse symbol names
                    for ((dispSymbol, binancePair) in cryptoPairMap) {
                        rawMap[binancePair]?.let { prices[dispSymbol] = it }
                    }
                }
            }
        } catch (_: Exception) {
            // Graceful fallback if network is restricted or offline
        }
        prices
    }
}
