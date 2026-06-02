package com.imdumb.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.imdumb.BuildConfig
import com.imdumb.data.cache.CategoryCache
import com.imdumb.data.cache.ConfigCache
import com.imdumb.data.datasources.MovieApiService
import com.imdumb.data.repositories.MovieRepositoryImpl
import com.imdumb.domain.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.ENABLE_LOGS) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("imdumb_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideApiKey(): String = BuildConfig.API_KEY

    @Provides
    @Singleton
    fun provideCategoryCache(sharedPreferences: SharedPreferences, gson: Gson): CategoryCache {
        return CategoryCache(sharedPreferences, gson)
    }

    @Provides
    @Singleton
    fun provideConfigCache(sharedPreferences: SharedPreferences): ConfigCache {
        return ConfigCache(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        apiService: MovieApiService,
        cache: CategoryCache,
        apiKey: String
    ): MovieRepository {
        return MovieRepositoryImpl(apiService, cache, apiKey)
    }
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}