plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    compileSdkVersion(Deps.compileSdkVersion)
    buildToolsVersion = Deps.buildToolsVersion

    defaultConfig {
        applicationId = "com.nik.capko.memo"
        minSdkVersion(Deps.minSdkVersion)
        targetSdkVersion(Deps.targetSdkVersion)
        versionCode = Deps.versionCode
        versionName = Deps.versionName

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
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

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Deps.kotlinVersion}")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.android.material:material:1.4.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:${Deps.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Deps.retrofitVersion}")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:${Deps.okhttpVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Deps.okhttpVersion}")

    // room
    implementation("androidx.room:room-runtime:${Deps.roomVersion}")
    implementation("androidx.room:room-ktx:${Deps.roomVersion}")
    kapt("androidx.room:room-compiler:${Deps.roomVersion}")

    // moxy
    implementation("com.github.moxy-community:moxy:${Deps.moxyVersion}")
    implementation("com.github.moxy-community:moxy-androidx:${Deps.moxyVersion}")
    implementation("com.github.moxy-community:moxy-ktx:${Deps.moxyVersion}")
    kapt("com.github.moxy-community:moxy-compiler:${Deps.moxyVersion}")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:${Deps.daggerHiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${Deps.daggerHiltVersion}")

    // ViewBindingPropertyDelegate
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.4.4")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Deps.coroutineVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Deps.coroutineVersion}")

    // rx binding
    implementation("com.jakewharton.rxbinding2:rxbinding:2.0.0")

    // lottie animation
    implementation("com.airbnb.android:lottie:3.7.0")

    implementation("com.intuit.sdp:sdp-android:1.0.6")

    // navigation cicerone
    implementation("com.github.terrakok:cicerone:6.6")

    // rxbinding
    implementation("com.jakewharton.rxbinding2:rxbinding:2.0.0")
}
