plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("com.nikcapko.memo.plugins.android-application") {
            id = "memo-android-application"
            implementationClass =
                "com.nikcapko.memo.plugins.AndroidApplicationPlugin"
        }
        register("com.nikcapko.memo.plugins.android-library") {
            id = "memo-android-library"
            implementationClass =
                "com.nikcapko.memo.plugins.AndroidLibraryPlugin"
        }
        register("com.nikcapko.memo.plugins.kotlin-jvm-library") {
            id = "memo-kotlin-jvm-library"
            implementationClass =
                "com.nikcapko.memo.plugins.KotlinJVMLibraryPlugin"
        }
    }
}

dependencies {
    gradleApi()

    implementation(libs.plugin.gradle)
    implementation(libs.plugin.kotlin)
}
