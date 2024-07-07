plugins {
    alias(libs.plugins.memo.android.library)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.nikcapko.memo.core.ui"

    buildFeatures {
        compose = true
        viewBinding = true
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    implementation(projects.coreCommon)

    implementation(libs.kotlin)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.material)

    implementation(libs.androidx.fragment.ktx)

    // lottie animation
    implementation(libs.lottie)

    // sdp-android
    implementation(libs.sdp)

    // navigation cicerone
    implementation(libs.cicerone)

    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.unit)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material)
    debugImplementation(libs.compose.ui.tooling)
}
