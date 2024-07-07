plugins {
    alias(libs.plugins.memo.kotlin.jvm.library)
}

dependencies {
    // coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
}
