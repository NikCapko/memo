plugins {
    id("kotlin-android")
    id("com.android.library")
    id("com.ibotta.gradle.aop")
}

android {
    compileSdk = libs.versions.compileSdkVersion.get().toInt()
    namespace = "com.nikcapko.memo.logger"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOf("-Xsam-conversions=class")
    }
}

dependencies {
    // AspectJ
    implementation(libs.aspectjrt)
}
