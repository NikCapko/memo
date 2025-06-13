plugins {
    alias(libs.plugins.memo.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.nikcapko.memo.app"

    buildFeatures { viewBinding = true }

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

    implementation(projects.coreCommon)
    implementation(projects.coreUi)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.presentation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material)

    // Dagger - Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // navigation cicerone
    implementation(libs.cicerone)

    // testing
    testImplementation(libs.test.assertj)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.junit)
    testImplementation(libs.test.junit.jupiter)
    testImplementation(libs.test.kotlinx.coroutines)
    testImplementation(libs.test.androidx.testing)
}
