package com.example.data

import android.content.Context
import android.content.SharedPreferences

object AppConfig {
    private const val PREFS_NAME = "signalverse_config"
    private const val KEY_SUPABASE_URL = "cfg_supabase_url"
    private const val KEY_SUPABASE_ANON_KEY = "cfg_supabase_anon_key"

    // Default or injected from BuildConfig if present
    private var cachedUrl: String? = null
    private var cachedAnonKey: String? = null

    fun getSupabaseUrl(context: Context): String {
        val prefs = getPrefs(context)
        val custom = prefs.getString(KEY_SUPABASE_URL, "") ?: ""
        if (custom.isNotBlank()) return custom.trim()

        if (cachedUrl != null) return cachedUrl!!
        cachedUrl = try {
            val buildConfigClass = Class.forName("com.example.BuildConfig")
            val field = buildConfigClass.getField("SUPABASE_URL")
            (field.get(null) as? String)?.trim() ?: ""
        } catch (_: Exception) {
            ""
        }
        return cachedUrl ?: ""
    }

    fun getSupabaseAnonKey(context: Context): String {
        val prefs = getPrefs(context)
        val custom = prefs.getString(KEY_SUPABASE_ANON_KEY, "") ?: ""
        if (custom.isNotBlank()) return custom.trim()

        if (cachedAnonKey != null) return cachedAnonKey!!
        cachedAnonKey = try {
            val buildConfigClass = Class.forName("com.example.BuildConfig")
            val field = buildConfigClass.getField("SUPABASE_ANON_KEY")
            (field.get(null) as? String)?.trim() ?: ""
        } catch (_: Exception) {
            ""
        }
        return cachedAnonKey ?: ""
    }

    fun setCustomSupabaseConfig(context: Context, url: String, anonKey: String) {
        getPrefs(context).edit()
            .putString(KEY_SUPABASE_URL, url.trim())
            .putString(KEY_SUPABASE_ANON_KEY, anonKey.trim())
            .apply()
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

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}
