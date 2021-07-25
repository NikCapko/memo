// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Dependencies.gradlePluginVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Dependencies.kotlinPluginVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Dependencies.daggerHiltVersion}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Dependencies.detektPluginVersion}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    apply("$rootDir/detekt.gradle")
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.google.com") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
        maven { url = uri("https://dl.bintray.com/terrakok/terramaven/") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
