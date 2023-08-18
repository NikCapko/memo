plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    compileSdk = AppConfig.compileSdkVersion
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.kotlin)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.material)

    implementation(libs.androidx.fragment.ktx)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // okhttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // room
    implementation(libs.room)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // moxy
    implementation(libs.moxy)
    implementation(libs.moxy.androidx)
    implementation(libs.moxy.ktx)
    kapt(libs.moxy.compiler)

    // Dagger - Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // ViewBindingPropertyDelegate
    implementation(libs.viewbindingpropertydelegate)

    // coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // rx binding
    implementation(libs.rxbinding)

    // lottie animation
    implementation(libs.lottie)

    // sdp-android
    implementation(libs.sdp)

    // navigation cicerone
    implementation(libs.cicerone)

    // androidx.lifecycle
    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    implementation(libs.mutableLiveEvent)

    // testing
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.junit)
    testImplementation(libs.test.junit.jupiter)
    testImplementation(libs.test.kotlinx.coroutines)
    testImplementation(libs.test.kotest)
}
android.testOptions {
    unitTests.all {
        it.useJUnitPlatform()
    }
    unitTests.isReturnDefaultValues = true
}
