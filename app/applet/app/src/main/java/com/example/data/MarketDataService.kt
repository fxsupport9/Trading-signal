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
            val url =
                "https://api.binance.com/api/v3/ticker/price?symbols=[\"BTCUSDT\",\"ETHUSDT\",\"SOLUSDT\",\"XRPUSDT\",\"LTCUSDT\",\"ADAUSDT\",\"DOGEUSDT\",\"BNBUSDT\"]"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "SignalVerse-Android/1.0")
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                if (body != null) {
                    val array = JSONArray(body)
                    val symbolPriceMap = mutableMapOf<String, Double>()
                    for (i in 0 until array.length()) {
                        val item = array.getJSONObject(i)
                        val s = item.getString("symbol")
                        val p = item.getString("price").toDoubleOrNull()
                        if (p != null) {
                            symbolPriceMap[s] = p
                        }
                    }
                    for ((displaySymbol, pair) in cryptoPairMap) {
                        val price = symbolPriceMap[pair]
                        if (price != null) {
                            prices[displaySymbol] = price
                        }
                    }
                }
            }
            response.close()
        } catch (_: Exception) {
            // Silently fallback if offline or API blocked
        }
        prices
    }
}
