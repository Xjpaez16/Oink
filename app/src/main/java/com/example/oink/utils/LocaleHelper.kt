package com.example.oink.utils

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleHelper {
    private const val PREFS_NAME = "oink_prefs"
    private const val KEY_LANGUAGE = "pref_language"

    fun getSavedLanguage(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, null)
    }

    private fun persistLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
    }

    fun getCurrentLanguage(context: Context): String {
        // Prefer persisted language when available
        getSavedLanguage(context)?.let { return it }

        val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
        return currentLocale.language
    }

    fun applyLocaleToContext(base: Context): Context {
        val lang = getSavedLanguage(base) ?: return base

        // Apply AppCompatDelegate for broad compatibility
        val appLocale = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(appLocale)

        // For older APIs, create a wrapped context with updated configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val locale = Locale.forLanguageTag(lang)
            val config = Configuration(base.resources.configuration)
            config.setLocale(locale)
            base.createConfigurationContext(config)
        } else {
            base
        }
    }

    fun setNewLocale(context: Context, languageTag: String) {
        persistLanguage(context, languageTag)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(languageTag)
        } else {
            val appLocale = LocaleListCompat.forLanguageTags(languageTag)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        // Recreate Activity to apply resources immediately
        (context as? Activity)?.runOnUiThread {
            context.recreate()
        }
    }

    fun changeLanguage(context: Context) {
        val current = getCurrentLanguage(context)
        val newLang = if (current.startsWith("es")) "en" else "es"
        setNewLocale(context, newLang)
    }
}
