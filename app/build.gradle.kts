plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    packagingOptions {
        pickFirst("META-INF/LICENSE.md")
        pickFirst("META-INF/LICENSE-notice.md")
    }

    compileSdk = libs.versions.compileSdkVersion.get().toInt()
    buildToolsVersion = libs.versions.buildToolsVersion.get()
    namespace = "com.nikcapko.memo"

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdkVersion.get().toInt()
        targetSdk = libs.versions.targetSdkVersion.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "com.nikcapko.memo.ui.HiltAndroidTestRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOf("-Xsam-conversions=class")
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
        unitTests.isReturnDefaultValues = true
    }
}

hilt {
    enableAggregatingTask = false
    enableExperimentalClasspathAggregation = true
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

    // Dagger - Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

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

    // testing
    testImplementation(libs.test.assertj)
    testImplementation(libs.test.junit)
    testImplementation(libs.test.junit.jupiter)
    testImplementation(libs.test.kotlinx.coroutines)
    testImplementation(libs.test.androidx.testing)
    testImplementation(libs.test.turbine)

    androidTestImplementation(libs.test.junit.androidx)
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.test.espresso.contrib)
    androidTestImplementation("org.hamcrest:hamcrest:2.2")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation(libs.test.kotlinx.coroutines)
    androidTestImplementation(libs.test.androidx.testing)
    androidTestImplementation(libs.hilt.testing)
    kspAndroidTest(libs.hilt.compiler)

    debugImplementation(libs.test.mockk.android)
}
