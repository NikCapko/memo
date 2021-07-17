plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    compileSdkVersion(AppConfig.compileSdkVersion)
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
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

    testImplementation("junit:junit:${Dependencies.junitVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Dependencies.androidxJunitVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Dependencies.espressoVersion}")

    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependencies.kotlinVersion}")

    implementation("androidx.core:core-ktx:${Dependencies.coreKtxVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${Dependencies.constraintLayoutVersion}")
    implementation("androidx.recyclerview:recyclerview:${Dependencies.recyclerviewVersion}")
    implementation("com.google.android.material:material:${Dependencies.materialVersion}")

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

    // moxy
    implementation("com.github.moxy-community:moxy:${Dependencies.moxyVersion}")
    implementation("com.github.moxy-community:moxy-androidx:${Dependencies.moxyVersion}")
    implementation("com.github.moxy-community:moxy-ktx:${Dependencies.moxyVersion}")
    kapt("com.github.moxy-community:moxy-compiler:${Dependencies.moxyVersion}")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:${Dependencies.daggerHiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${Dependencies.daggerHiltVersion}")

    // ViewBindingPropertyDelegate
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Dependencies.viewBindingPropertyDelegateVersion}")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.coroutineVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Dependencies.coroutineVersion}")

    // rx binding
    implementation("com.jakewharton.rxbinding2:rxbinding:${Dependencies.rxBindingVersion}")

    // lottie animation
    implementation("com.airbnb.android:lottie:${Dependencies.lottieAnimationVersion}")

    // sdp-android
    implementation("com.intuit.sdp:sdp-android:${Dependencies.sdpAndroidVersion}")

    // navigation cicerone
    implementation("com.github.terrakok:cicerone:${Dependencies.ciceroneVersion}")
}
