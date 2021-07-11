// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Deps.gradleVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Deps.kotlinPluginVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Deps.daggerHiltVersion}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Deps.detektVersion}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    apply("$rootDir/detekt.gradle")
    repositories {
        google()
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
        maven { url = uri("https://dl.bintray.com/terrakok/terramaven/") }
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
