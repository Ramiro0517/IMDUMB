package com.imdumb.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.imdumb.R
import com.imdumb.data.cache.ConfigCache
import com.imdumb.databinding.ActivitySplashBinding
import com.imdumb.presentation.categories.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var configCache: ConfigCache

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadRemoteConfig()
    }

    private fun loadRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val apiTimeout = remoteConfig.getLong("api_timeout")
                    val enableRecommendations = remoteConfig.getBoolean("enable_recommendations")
                    val splashDuration = remoteConfig.getLong("splash_duration")

                    configCache.saveConfig(apiTimeout, enableRecommendations, splashDuration)

                } else {
                }
                val duration = if (task.isSuccessful) {
                    remoteConfig.getLong("splash_duration")
                } else {
                    configCache.getSplashDuration()
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, duration)
            }
    }
}