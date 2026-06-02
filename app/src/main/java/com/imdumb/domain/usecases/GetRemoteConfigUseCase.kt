package com.imdumb.domain.usecases

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.Single
import javax.inject.Inject

data class AppConfig(
    val apiTimeout: Long,
    val enableRecommendations: Boolean,
    val splashDuration: Long
)

class GetRemoteConfigUseCase @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {

    operator fun invoke(): Single<AppConfig> {
        return Single.create { emitter ->
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val config = AppConfig(
                            apiTimeout = remoteConfig.getLong("api_timeout"),
                            enableRecommendations = remoteConfig.getBoolean("enable_recommendations"),
                            splashDuration = remoteConfig.getLong("splash_duration")
                        )
                        emitter.onSuccess(config)
                    } else {
                        // Valores por defecto
                        val config = AppConfig(
                            apiTimeout = 30000,
                            enableRecommendations = true,
                            splashDuration = 2000
                        )
                        emitter.onSuccess(config)
                    }
                }
                .addOnFailureListener { error ->
                    emitter.onError(error)
                }
        }
    }
}