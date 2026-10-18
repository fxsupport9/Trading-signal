package com.example.data

import android.content.Context
import com.example.model.AccountManagementRequest
import com.example.model.CopyTradingRequest
import com.example.model.ServiceOption
import com.example.model.SubmissionMethod
import com.example.model.UserProfile
import org.json.JSONArray
import org.json.JSONObject

class LocalDataStore(private val context: Context) {
    private val prefs = context.getSharedPreferences("signalverse_data", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_PROFILE = "user_profile_json"
        private const val KEY_COPY_REQUESTS = "copy_requests_json"
        private const val KEY_MGMT_REQUESTS = "mgmt_requests_json"
    }

    private fun JSONObject.optStringOrNull(key: String): String? {
        return if (has(key) && !isNull(key)) getString(key) else null
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
        // Prevent duplicates with same loginId
        list.removeAll { it.loginId == req.loginId && it.platform == req.platform }
        list.add(0, req)

        val array = JSONArray()
        list.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("fullName", item.fullName)
                put("telegramUsername", item.telegramUsername)
                put("whatsappNumber", item.whatsappNumber)
                put("platform", item.platform)
                put("loginId", item.loginId)
                put("brokerName", item.brokerName)
                put("brokerServer", item.brokerServer)
                put("passwordProtected", "••••••••") // masked
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
        val list = mutableListOf<CopyTradingRequest>()
        try {
            val array = JSONArray(str)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    CopyTradingRequest(
                        id = obj.getString("id"),
                        fullName = obj.getString("fullName"),
                        telegramUsername = obj.getString("telegramUsername"),
                        whatsappNumber = obj.getString("whatsappNumber"),
                        platform = obj.getString("platform"),
                        loginId = obj.getString("loginId"),
                        brokerName = obj.getString("brokerName"),
                        brokerServer = obj.getString("brokerServer"),
                        passwordProtected = obj.optString("passwordProtected", "••••••••"),
                        additionalNotes = obj.optString("additionalNotes", ""),
                        screenshotUri = obj.optStringOrNull("screenshotUri"),
                        submissionDate = obj.optLong("submissionDate", System.currentTimeMillis()),
                        status = obj.optString("status", "Pending")
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }

    fun saveAccountManagementRequest(req: AccountManagementRequest) {
        val list = getAccountManagementRequests().toMutableList()
        list.add(0, req)

        val array = JSONArray()
        list.forEach { item ->
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
                put("tradingPasswordProtected", "••••••••")
                put("accountBalance", item.accountBalance)
                put("brokerWebsiteOrApp", item.brokerWebsiteOrApp)
                put("brokerEmail", item.brokerEmail)
                put("brokerPasswordProtected", "••••••••")
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
        val list = mutableListOf<AccountManagementRequest>()
        try {
            val array = JSONArray(str)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val opt = try {
                    ServiceOption.valueOf(obj.getString("serviceOption"))
                } catch (_: Exception) {
                    ServiceOption.ACCOUNT_MANAGEMENT
                }
                val method = try {
                    SubmissionMethod.valueOf(obj.getString("submissionMethod"))
                } catch (_: Exception) {
                    SubmissionMethod.TRADING_ACCOUNT
                }
                list.add(
                    AccountManagementRequest(
                        id = obj.getString("id"),
                        serviceOption = opt,
                        submissionMethod = method,
                        fullName = obj.getString("fullName"),
                        whatsappNumber = obj.getString("whatsappNumber"),
                        telegramUsername = obj.getString("telegramUsername"),
                        brokerName = obj.optStringOrNull("brokerName"),
                        platform = obj.optStringOrNull("platform"),
                        brokerServer = obj.optStringOrNull("brokerServer"),
                        loginId = obj.optStringOrNull("loginId"),
                        tradingPasswordProtected = obj.optStringOrNull("tradingPasswordProtected"),
                        accountBalance = obj.optStringOrNull("accountBalance"),
                        brokerWebsiteOrApp = obj.optStringOrNull("brokerWebsiteOrApp"),
                        brokerEmail = obj.optStringOrNull("brokerEmail"),
                        brokerPasswordProtected = obj.optStringOrNull("brokerPasswordProtected"),
                        additionalNotes = obj.optString("additionalNotes", ""),
                        screenshotUri = obj.optStringOrNull("screenshotUri"),
                        submissionDate = obj.optLong("submissionDate", System.currentTimeMillis()),
                        status = obj.optString("status", "Pending")
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }
}
