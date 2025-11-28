package com.example.oink.utils

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LocaleHelper {
    fun changeLanguage(context: Context) {
        // 1. Detectar idioma actual
        val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }

        // 2. Cambiar al opuesto (Si es Español -> Inglés, Si es Inglés -> Español)
        val newLanguageCode = if (currentLocale.language == "es") "en" else "es"

        // 3. Aplicar cambio
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(newLanguageCode)
        } else {
            val appLocale = LocaleListCompat.forLanguageTags(newLanguageCode)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }
}
