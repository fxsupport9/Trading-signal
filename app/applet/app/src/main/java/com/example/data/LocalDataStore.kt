package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AccountManagementRequest
import com.example.model.CopyTradingRequest
import com.example.model.ServiceOption
import com.example.model.SubmissionMethod
import com.example.model.UserProfile
import org.json.JSONArray
import org.json.JSONObject

class LocalDataStore(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("signalverse_data", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_PROFILE = "user_profile_json"
        private const val KEY_COPY_REQUESTS = "copy_requests_json"
        private const val KEY_MGMT_REQUESTS = "mgmt_requests_json"
    }

    private fun JSONObject.optStringOrNull(key: String): String? {
        if (!has(key) || isNull(key)) return null
        return getString(key)
    }

    fun saveUserProfile(profile: UserProfile?) {
        if (profile == null) {
            prefs.edit().remove(KEY_USER_PROFILE).apply()
            return
        }
        val json = JSONObject().apply {
            put("id", profile.id)
            put("email", profile.email)
            put("fullName", profile.fullName)
            put("accountStatus", profile.accountStatus)
            put("tier", profile.tier)
            put("token", profile.token)
            put("createdAt", profile.createdAt)
        }
        prefs.edit().putString(KEY_USER_PROFILE, json.toString()).apply()
    }

    fun getUserProfile(): UserProfile? {
        val str = prefs.getString(KEY_USER_PROFILE, null) ?: return null
        return try {
            val json = JSONObject(str)
            UserProfile(
                id = json.getString("id"),
                email = json.getString("email"),
                fullName = json.getString("fullName"),
                accountStatus = json.optString("accountStatus", "Active"),
                tier = json.optString("tier", "Free Trader"),
                token = json.optStringOrNull("token"),
                createdAt = json.optLong("createdAt", System.currentTimeMillis())
            )
        } catch (_: Exception) {
            null
        }
    }

    fun saveCopyRequest(req: CopyTradingRequest) {
        val list = getCopyRequests().toMutableList()
        list.add(0, req)
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("fullName", item.fullName)
                put("whatsappNumber", item.whatsappNumber)
                put("telegramUsername", item.telegramUsername)
                put("brokerName", item.brokerName)
                put("platform", item.platform)
                put("brokerServer", item.brokerServer)
                put("loginId", item.loginId)
                put("passwordProtected", item.passwordProtected)
                put("additionalNotes", item.additionalNotes)
                put("screenshotUri", item.screenshotUri)
                put("submissionDate", item.submissionDate)
                put("status", item.status)
            }
            array.put(obj)
        }
        prefs.edit().putString(KEY_COPY_REQUESTS, array.toString()).apply()
    }

    fun getCopyRequests(): List<CopyTradingRequest> {
        val str = prefs.getString(KEY_COPY_REQUESTS, null) ?: return emptyList()
        return try {
            val array = JSONArray(str)
            val list = mutableListOf<CopyTradingRequest>()
            for (i in 0 until array.length()) {
                val json = array.getJSONObject(i)
                list.add(
                    CopyTradingRequest(
                        id = json.getString("id"),
                        fullName = json.getString("fullName"),
                        whatsappNumber = json.getString("whatsappNumber"),
                        telegramUsername = json.getString("telegramUsername"),
                        brokerName = json.getString("brokerName"),
                        platform = json.getString("platform"),
                        brokerServer = json.getString("brokerServer"),
                        loginId = json.getString("loginId"),
                        passwordProtected = json.getString("passwordProtected"),
                        additionalNotes = json.optString("additionalNotes", ""),
                        screenshotUri = json.optStringOrNull("screenshotUri"),
                        submissionDate = json.optLong("submissionDate", System.currentTimeMillis()),
                        status = json.optString("status", "Pending")
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveAccountManagementRequest(req: AccountManagementRequest) {
        val list = getAccountManagementRequests().toMutableList()
        list.add(0, req)
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("serviceOption", item.serviceOption.name)
                put("submissionMethod", item.submissionMethod.name)
                put("fullName", item.fullName)
                put("whatsappNumber", item.whatsappNumber)
                put("telegramUsername", item.telegramUsername)
                put("brokerName", item.brokerName)
                put("platform", item.platform)
                put("brokerServer", item.brokerServer)
                put("loginId", item.loginId)
                put("tradingPasswordProtected", item.tradingPasswordProtected)
                put("accountBalance", item.accountBalance)
                put("brokerWebsiteOrApp", item.brokerWebsiteOrApp)
                put("brokerEmail", item.brokerEmail)
                put("brokerPasswordProtected", item.brokerPasswordProtected)
                put("additionalNotes", item.additionalNotes)
                put("screenshotUri", item.screenshotUri)
                put("submissionDate", item.submissionDate)
                put("status", item.status)
            }
            array.put(obj)
        }
        prefs.edit().putString(KEY_MGMT_REQUESTS, array.toString()).apply()
    }

    fun getAccountManagementRequests(): List<AccountManagementRequest> {
        val str = prefs.getString(KEY_MGMT_REQUESTS, null) ?: return emptyList()
        return try {
            val array = JSONArray(str)
            val list = mutableListOf<AccountManagementRequest>()
            for (i in 0 until array.length()) {
                val json = array.getJSONObject(i)
                val optName = json.optString("serviceOption", ServiceOption.ACCOUNT_MANAGEMENT.name)
                val serviceOption = try {
                    ServiceOption.valueOf(optName)
                } catch (_: Exception) {
                    ServiceOption.ACCOUNT_MANAGEMENT
                }
                val methodStr = json.optString("submissionMethod", SubmissionMethod.TRADING_ACCOUNT.name)
                val submissionMethod = try {
                    SubmissionMethod.valueOf(methodStr)
                } catch (_: Exception) {
                    SubmissionMethod.TRADING_ACCOUNT
                }
                list.add(
                    AccountManagementRequest(
                        id = json.getString("id"),
                        serviceOption = serviceOption,
                        submissionMethod = submissionMethod,
                        fullName = json.getString("fullName"),
                        whatsappNumber = json.getString("whatsappNumber"),
                        telegramUsername = json.getString("telegramUsername"),
                        brokerName = json.getString("brokerName"),
                        platform = json.getString("platform"),
                        brokerServer = json.getString("brokerServer"),
                        loginId = json.getString("loginId"),
                        tradingPasswordProtected = json.optString("tradingPasswordProtected", ""),
                        accountBalance = json.optString("accountBalance", ""),
                        brokerWebsiteOrApp = json.optString("brokerWebsiteOrApp", ""),
                        brokerEmail = json.optString("brokerEmail", ""),
                        brokerPasswordProtected = json.optString("brokerPasswordProtected", ""),
                        additionalNotes = json.optString("additionalNotes", ""),
                        screenshotUri = json.optStringOrNull("screenshotUri"),
                        submissionDate = json.optLong("submissionDate", System.currentTimeMillis()),
                        status = json.optString("status", "Pending")
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }
}
