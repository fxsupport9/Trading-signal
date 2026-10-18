package com.example.data

import android.content.Context
import com.example.model.AccountManagementRequest
import com.example.model.CopyTradingRequest
import com.example.model.ServiceOption
import com.example.model.SignalCategory
import com.example.model.SignalStatus
import com.example.model.SubmissionMethod
import com.example.model.TradeDirection
import com.example.model.TradingSignal
import com.example.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class SupabaseClient(private val context: Context) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private fun baseUrl(): String = AppConfig.getSupabaseUrl(context).trimEnd('/')
    private fun anonKey(): String = AppConfig.getSupabaseAnonKey(context).trim()

    val isConfigured: Boolean
        get() = AppConfig.isSupabaseConfigured(context)

    suspend fun testConnection(): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext false to "Supabase URL and Anon Key are not set."
        }
        try {
            val url = "${baseUrl()}/rest/v1/"
            val request = Request.Builder()
                .url(url)
                .header("apikey", anonKey())
                .header("Authorization", "Bearer ${anonKey()}")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful || response.code == 404 || response.code == 200) {
                    true to "Connected successfully to Supabase API."
                } else {
                    false to "Supabase returned HTTP ${response.code}: ${response.message}"
                }
            }
        } catch (e: Exception) {
            false to "Connection failed: ${e.localizedMessage ?: "Unknown network error"}"
        }
    }

    // ==========================================
    // SUPABASE AUTH
    // ==========================================
    suspend fun signIn(email: String, pass: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext Result.failure(Exception("Supabase is not configured. Please set SUPABASE_URL and SUPABASE_ANON_KEY."))
        }
        try {
            val url = "${baseUrl()}/auth/v1/token?grant_type=password"
            val json = JSONObject().apply {
                put("email", email.trim())
                put("password", pass)
            }
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .header("apikey", anonKey())
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                val responseStr = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val jsonObj = JSONObject(responseStr)
                    val accessToken = jsonObj.optString("access_token")
                    val userObj = jsonObj.optJSONObject("user")
                    val id = userObj?.optString("id") ?: "user_${System.currentTimeMillis()}"
                    val userEmail = userObj?.optString("email") ?: email
                    val meta = userObj?.optJSONObject("user_metadata")
                    val name = meta?.optString("full_name") ?: userEmail.substringBefore("@")

                    val profile = UserProfile(
                        id = id,
                        email = userEmail,
                        fullName = name,
                        accountStatus = "Active",
                        tier = "VIP Member",
                        token = accessToken
                    )
                    Result.success(profile)
                } else {
                    val errorDesc = try {
                        JSONObject(responseStr).optString("error_description", response.message)
                    } catch (_: Exception) {
                        response.message
                    }
                    Result.failure(Exception(errorDesc))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(name: String, email: String, pass: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext Result.failure(Exception("Supabase is not configured. Please set SUPABASE_URL and SUPABASE_ANON_KEY."))
        }
        try {
            val url = "${baseUrl()}/auth/v1/signup"
            val meta = JSONObject().apply {
                put("full_name", name.trim())
            }
            val json = JSONObject().apply {
                put("email", email.trim())
                put("password", pass)
                put("data", meta)
            }
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .header("apikey", anonKey())
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                val responseStr = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val jsonObj = JSONObject(responseStr)
                    val accessToken = if (jsonObj.has("access_token") && !jsonObj.isNull("access_token")) jsonObj.getString("access_token") else null
                    val id = jsonObj.optString("id", "user_${System.currentTimeMillis()}")
                    val profile = UserProfile(
                        id = id,
                        email = email.trim(),
                        fullName = name.trim(),
                        accountStatus = "Active",
                        tier = "Free Trader",
                        token = accessToken
                    )
                    Result.success(profile)
                } else {
                    val errorDesc = try {
                        JSONObject(responseStr).optString("msg", response.message)
                    } catch (_: Exception) {
                        response.message
                    }
                    Result.failure(Exception(errorDesc))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String): Result<String> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            return@withContext Result.failure(Exception("Supabase is not configured."))
        }
        try {
            val url = "${baseUrl()}/auth/v1/recover"
            val json = JSONObject().apply {
                put("email", email.trim())
            }
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .header("apikey", anonKey())
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Result.success("Password recovery email sent successfully.")
                } else {
                    Result.failure(Exception("Failed to send recovery email (HTTP ${response.code})."))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==========================================
    // SIGNALS
    // ==========================================
    suspend fun fetchSignals(category: SignalCategory? = null): Result<List<TradingSignal>> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            // Return empty list so the app renders the required professional empty state!
            return@withContext Result.success(emptyList())
        }
        try {
            var url = "${baseUrl()}/rest/v1/signals?select=*&order=created_at.desc"
            if (category != null) {
                url += "&category=eq.${category.name}"
            }
            val request = Request.Builder()
                .url(url)
                .header("apikey", anonKey())
                .header("Authorization", "Bearer ${anonKey()}")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                val responseStr = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val list = mutableListOf<TradingSignal>()
                    val array = JSONArray(responseStr)
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val id = obj.optString("id", "sig_$i")
                        val symbol = obj.optString("symbol", "")
                        val catStr = obj.optString("category", "COMMODITIES")
                        val dirStr = obj.optString("direction", "BUY")
                        val entry = obj.optDouble("entry_price", 0.0)
                        val sl = obj.optDouble("stop_loss", 0.0)
                        val tp1 = if (obj.has("tp1") && !obj.isNull("tp1")) obj.getDouble("tp1") else null
                        val tp2 = if (obj.has("tp2") && !obj.isNull("tp2")) obj.getDouble("tp2") else null
                        val tp3 = if (obj.has("tp3") && !obj.isNull("tp3")) obj.getDouble("tp3") else null
                        val statusStr = obj.optString("status", "OPEN")
                        val isVip = obj.optBoolean("is_vip", false)
                        val notes = if (obj.has("notes") && !obj.isNull("notes")) obj.getString("notes") else null

                        val cat = try { SignalCategory.valueOf(catStr) } catch (_: Exception) { SignalCategory.COMMODITIES }
                        val dir = try { TradeDirection.valueOf(dirStr) } catch (_: Exception) { TradeDirection.BUY }
                        val status = when (statusStr.uppercase()) {
                            "TP_HIT", "TP HIT" -> SignalStatus.TP_HIT
                            "SL_HIT", "SL HIT" -> SignalStatus.SL_HIT
                            "CLOSED" -> SignalStatus.CLOSED
                            else -> SignalStatus.OPEN
                        }

                        list.add(
                            TradingSignal(
                                id = id,
                                symbol = symbol,
                                category = cat,
                                direction = dir,
                                entryPrice = entry,
                                stopLoss = sl,
                                tp1 = tp1,
                                tp2 = tp2,
                                tp3 = tp3,
                                status = status,
                                isVip = isVip,
                                notes = notes
                            )
                        )
                    }
                    Result.success(list)
                } else {
                    Result.failure(Exception("Failed to fetch signals from Supabase (HTTP ${response.code})"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Allows seeding sample signals directly into connected Supabase for instant real testing
    suspend fun seedSampleSignalsToSupabase(): Result<Int> = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext Result.failure(Exception("Supabase is not configured."))
        try {
            val starterSignals = listOf(
                JSONObject().apply {
                    put("symbol", "XAUUSD — Gold")
                    put("category", "COMMODITIES")
                    put("direction", "BUY")
                    put("entry_price", 2658.40)
                    put("stop_loss", 2642.00)
                    put("tp1", 2675.00)
                    put("tp2", 2690.00)
                    put("tp3", 2715.00)
                    put("status", "OPEN")
                    put("is_vip", false)
                    put("notes", "Bullish breakout on 4H candlestick above ascending trendline")
                },
                JSONObject().apply {
                    put("symbol", "BTCUSD — Bitcoin")
                    put("category", "CRYPTO")
                    put("direction", "BUY")
                    put("entry_price", 67850.00)
                    put("stop_loss", 66200.00)
                    put("tp1", 69400.00)
                    put("tp2", 71200.00)
                    put("tp3", 73500.00)
                    put("status", "OPEN")
                    put("is_vip", true)
                    put("notes", "Institutional liquidity sweep at key weekly support")
                },
                JSONObject().apply {
                    put("symbol", "EURUSD")
                    put("category", "COMMODITIES")
                    put("direction", "SELL")
                    put("entry_price", 1.08450)
                    put("stop_loss", 1.08900)
                    put("tp1", 1.08050)
                    put("tp2", 1.07700)
                    put("tp3", 1.07200)
                    put("status", "TP_HIT")
                    put("is_vip", false)
                    put("notes", "Bearish rejection at 200 EMA resistance")
                },
                JSONObject().apply {
                    put("symbol", "ETHUSD — Ethereum")
                    put("category", "CRYPTO")
                    put("direction", "BUY")
                    put("entry_price", 2640.00)
                    put("stop_loss", 2570.00)
                    put("tp1", 2720.00)
                    put("tp2", 2810.00)
                    put("tp3", 2950.00)
                    put("status", "OPEN")
                    put("is_vip", false)
                    put("notes", "Double bottom pattern confirmed on 1H chart")
                },
                JSONObject().apply {
                    put("symbol", "US30")
                    put("category", "COMMODITIES")
                    put("direction", "BUY")
                    put("entry_price", 43250.0)
                    put("stop_loss", 42980.0)
                    put("tp1", 43550.0)
                    put("tp2", 43800.0)
                    put("tp3", 44100.0)
                    put("status", "CLOSED")
                    put("is_vip", true)
                    put("notes", "Macro index continuation setup")
                },
                JSONObject().apply {
                    put("symbol", "SOLUSD — Solana")
                    put("category", "CRYPTO")
                    put("direction", "BUY")
                    put("entry_price", 154.20)
                    put("stop_loss", 147.50)
                    put("tp1", 162.00)
                    put("tp2", 172.50)
                    put("tp3", 188.00)
                    put("status", "OPEN")
                    put("is_vip", true)
                    put("notes", "Ecosystem volume surging with high RSI momentum")
                }
            )

            val array = JSONArray()
            starterSignals.forEach { array.put(it) }

            val url = "${baseUrl()}/rest/v1/signals"
            val body = array.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .header("apikey", anonKey())
                .header("Authorization", "Bearer ${anonKey()}")
                .header("Prefer", "return=representation")
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Result.success(starterSignals.size)
                } else {
                    Result.failure(Exception("Supabase returned HTTP ${response.code} during seed. Ensure the 'signals' table exists."))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==========================================
    // SUBMIT MT5 COPY REQUEST
    // ==========================================
    suspend fun submitCopyRequest(req: CopyTradingRequest): Result<String> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            // Local mode fallback
            return@withContext Result.success("Saved to local offline queue (Supabase URL not configured)")
        }
        try {
            val json = JSONObject().apply {
                put("full_name", req.fullName)
                put("telegram_username", req.telegramUsername)
                put("whatsapp_number", req.whatsappNumber)
                put("platform", req.platform)
                put("login_id", req.loginId)
                put("broker_name", req.brokerName)
                put("broker_server", req.brokerServer)
                put("password_hash", req.passwordProtected.hashCode().toString()) // Never send or store plaintext
                put("additional_notes", req.additionalNotes)
                put("screenshot_uri", req.screenshotUri)
                put("status", "Pending")
            }

            val url = "${baseUrl()}/rest/v1/copy_requests"
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .header("apikey", anonKey())
                .header("Authorization", "Bearer ${anonKey()}")
                .header("Prefer", "return=representation")
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    // Trigger administrator notification if Edge Function is present
                    notifyAdmin("New MT5 Copy Request: ${req.platform} #${req.loginId} (${req.fullName})")
                    Result.success("Request successfully submitted to Supabase.")
                } else {
                    Result.failure(Exception("Supabase returned HTTP ${response.code}: ${response.message}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==========================================
    // SUBMIT ACCOUNT MANAGEMENT REQUEST
    // ==========================================
    suspend fun submitAccountManagementRequest(req: AccountManagementRequest): Result<String> = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            // Local mode fallback
            return@withContext Result.success("Saved to local offline queue (Supabase URL not configured)")
        }
        try {
            val json = JSONObject().apply {
                put("service_option", req.serviceOption.name)
                put("submission_method", req.submissionMethod.name)
                put("full_name", req.fullName)
                put("whatsapp_number", req.whatsappNumber)
                put("telegram_username", req.telegramUsername)
                put("broker_name", req.brokerName)
                put("platform", req.platform)
                put("broker_server", req.brokerServer)
                put("login_id", req.loginId)
                put("trading_password_hash", req.tradingPasswordProtected?.hashCode()?.toString())
                put("account_balance", req.accountBalance)
                put("broker_website_or_app", req.brokerWebsiteOrApp)
                put("broker_email", req.brokerEmail)
                put("broker_password_hash", req.brokerPasswordProtected?.hashCode()?.toString())
                put("additional_notes", req.additionalNotes)
                put("screenshot_uri", req.screenshotUri)
                put("status", "Pending")
            }

            val url = "${baseUrl()}/rest/v1/account_management_requests"
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .header("apikey", anonKey())
                .header("Authorization", "Bearer ${anonKey()}")
                .header("Prefer", "return=representation")
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    notifyAdmin("New Account Mgmt Request: ${req.serviceOption.title} (${req.fullName})")
                    Result.success("Account management request successfully submitted to Supabase.")
                } else {
                    Result.failure(Exception("Supabase returned HTTP ${response.code}: ${response.message}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Server-side Edge Function / webhook notification trigger
    private suspend fun notifyAdmin(message: String) = withContext(Dispatchers.IO) {
        try {
            val url = "${baseUrl()}/functions/v1/notify-admin"
            val json = JSONObject().apply {
                put("message", message)
                put("timestamp", System.currentTimeMillis())
            }
            val body = json.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .header("apikey", anonKey())
                .header("Authorization", "Bearer ${anonKey()}")
                .post(body)
                .build()
            client.newCall(request).execute().close()
        } catch (_: Exception) {
            // Notification edge function is optional
        }
    }
}
