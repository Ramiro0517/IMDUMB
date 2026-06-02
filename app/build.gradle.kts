plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    id("kotlin-kapt")
}

android {
    namespace = "com.imdumb"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.imdumb"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_KEY", "\"51a1f404c7aebe9206f365de0c0f0c02\"")
            buildConfigField("boolean", "ENABLE_LOGS", "true")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "API_KEY", "\"51a1f404c7aebe9206f365de0c0f0c02\"")
            buildConfigField("boolean", "ENABLE_LOGS", "false")
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true  // ✅ Esto es CRUCIAL
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.viewpager2)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava2)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.gson)

    // RxJava
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.rxkotlin)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.database)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.rxjava2)
    kapt(libs.room.compiler)

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}