plugins {
    alias(libs.plugins.memo.android.library)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.nikcapko.memo.core.test"
    packaging {
        resources {
            excludes += listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/licenses/ASM",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll",
            )
        }
    }
}

dependencies {
    implementation(projects.coreCommon)
    implementation(kotlin("reflect"))

    api(libs.test.assertj)
    api(libs.test.mockk)
    api(libs.test.junit)
    api(libs.test.junit.jupiter)
    api(libs.test.kotlinx.coroutines)
    api(libs.test.androidx.testing)
    api(libs.test.kotest.runner)
    api(libs.test.kotest.assertions)
}
