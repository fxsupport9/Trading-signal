package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig

object AppConfig {
    private const val PREFS_NAME = "signalverse_config"
    private const val KEY_SUPABASE_URL = "cfg_supabase_url"
    private const val KEY_SUPABASE_ANON_KEY = "cfg_supabase_anon_key"

    private var cachedUrl: String? = null
    private var cachedAnonKey: String? = null

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getSupabaseUrl(context: Context): String {
        val custom = getPrefs(context).getString(KEY_SUPABASE_URL, "") ?: ""
        if (custom.isNotBlank()) {
            return custom.trim()
        }
        if (cachedUrl != null) {
            return cachedUrl!!
        }
        val url = try {
            val buildConfigClass = Class.forName("com.example.BuildConfig")
            val field = buildConfigClass.getField("SUPABASE_URL")
            val value = field.get(null)
            if (value is String) value.trim() else ""
        } catch (_: Exception) {
            ""
        }
        cachedUrl = url
        return url
    }

    fun getSupabaseAnonKey(context: Context): String {
        val custom = getPrefs(context).getString(KEY_SUPABASE_ANON_KEY, "") ?: ""
        if (custom.isNotBlank()) {
            return custom.trim()
        }
        if (cachedAnonKey != null) {
            return cachedAnonKey!!
        }
        val key = try {
            val buildConfigClass = Class.forName("com.example.BuildConfig")
            val field = buildConfigClass.getField("SUPABASE_ANON_KEY")
            val value = field.get(null)
            if (value is String) value.trim() else ""
        } catch (_: Exception) {
            ""
        }
        cachedAnonKey = key
        return key
    }

    fun setCustomSupabaseConfig(context: Context, url: String, anonKey: String) {
        getPrefs(context).edit()
            .putString(KEY_SUPABASE_URL, url.trim())
            .putString(KEY_SUPABASE_ANON_KEY, anonKey.trim())
            .apply()
        cachedUrl = url.trim()
        cachedAnonKey = anonKey.trim()
    }

    fun isSupabaseConfigured(context: Context): Boolean {
        val url = getSupabaseUrl(context)
        val key = getSupabaseAnonKey(context)
        return url.isNotBlank() &&
                key.isNotBlank() &&
                url.startsWith("https://") &&
                !url.contains("placeholder") &&
                !key.contains("placeholder")
    }
}
