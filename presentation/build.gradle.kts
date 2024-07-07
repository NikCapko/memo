plugins {
    alias(libs.plugins.memo.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.nikcapko.memo.presentation"

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions { kotlinCompilerExtensionVersion = "2.0.0" }

    testOptions {
        unitTests.all { it.useJUnitPlatform() }
        unitTests.isReturnDefaultValues = true
    }
}

hilt {
    enableAggregatingTask = false
    enableExperimentalClasspathAggregation = true
}

dependencies {
    implementation(projects.coreCommon)
    implementation(projects.coreData)
    implementation(projects.coreUi)
    implementation(projects.domain)

    implementation(kotlin("reflect"))

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

    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.android)
    implementation(libs.compose.ui.unit)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.tooling.preview.android)

    // testing
    testImplementation(projects.coreTest)
}
