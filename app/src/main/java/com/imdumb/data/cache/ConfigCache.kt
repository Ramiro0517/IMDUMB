package com.imdumb.data.cache

import android.content.SharedPreferences
import javax.inject.Inject

class ConfigCache @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val KEY_API_TIMEOUT = "api_timeout"
        private const val KEY_ENABLE_RECOMMENDATIONS = "enable_recommendations"
        private const val KEY_SPLASH_DURATION = "splash_duration"
    }

    fun saveConfig(apiTimeout: Long, enableRecommendations: Boolean, splashDuration: Long) {
        sharedPreferences.edit()
            .putLong(KEY_API_TIMEOUT, apiTimeout)
            .putBoolean(KEY_ENABLE_RECOMMENDATIONS, enableRecommendations)
            .putLong(KEY_SPLASH_DURATION, splashDuration)
            .apply()
    }

    fun getApiTimeout(): Long {
        return sharedPreferences.getLong(KEY_API_TIMEOUT, 30000)
    }

    fun isRecommendationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_ENABLE_RECOMMENDATIONS, true)
    }

    fun getSplashDuration(): Long {
        return sharedPreferences.getLong(KEY_SPLASH_DURATION, 2000)
    }
}