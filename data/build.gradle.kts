plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            debuggable(false)
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
    implementation(project(":domain"))

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:${Dependencies.daggerHiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${Dependencies.daggerHiltVersion}")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Dependencies.retrofitVersion}")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:${Dependencies.okhttpVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Dependencies.okhttpVersion}")

    // room
    implementation("androidx.room:room-runtime:${Dependencies.roomVersion}")
    implementation("androidx.room:room-ktx:${Dependencies.roomVersion}")
    kapt("androidx.room:room-compiler:${Dependencies.roomVersion}")
}
