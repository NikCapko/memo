plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = AppConfig.compileSdkVersion
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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
    implementation(kotlin("reflect"))

    implementation(project(":core"))
    implementation(project(":domain"))

    // Dagger - Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

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

    // testing
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.junit)
    testImplementation(libs.test.junit.jupiter)
    testImplementation(libs.test.kotlinx.coroutines)
}

android.testOptions {
    unitTests.all {
        it.useJUnitPlatform()
    }
    unitTests.isReturnDefaultValues = true
}
