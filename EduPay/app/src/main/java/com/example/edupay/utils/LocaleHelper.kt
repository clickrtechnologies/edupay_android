package com.app.conatctsync.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.preference.PreferenceManager
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import java.util.*

object LocaleHelper {
    fun onAttach(context: Context): Context {
        val prefs=PreferenceHelper(context)
      //  val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val lang = prefs.getValue(Constants.SELECTED_LANGUAGE, "en") ?: "en"
        return setLocale(context, lang)
    }

    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return updateResources(context, language)
    }

    private fun persist(context: Context, language: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(Constants.SELECTED_LANGUAGE, language)
            .apply()
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }
}