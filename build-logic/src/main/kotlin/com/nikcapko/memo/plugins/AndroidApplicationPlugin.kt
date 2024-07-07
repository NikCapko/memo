package com.nikcapko.memo.plugins

import com.android.build.api.dsl.ApplicationExtension
import com.nikcapko.memo.applicationExtension
import com.nikcapko.memo.libs
import com.nikcapko.memo.pluginOf
import com.nikcapko.memo.versionOf
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.pluginOf("android-application"))
            apply(libs.pluginOf("kotlin-android"))
        }
        applicationExtension.configure(project)
    }

    private fun ApplicationExtension.configure(project: Project) {
        compileSdk = project.libs.versionOf("compileSdk").toInt()

        defaultConfig {
            applicationId = "com.nikcapko.memo"
            minSdk = project.libs.versionOf("minSdk").toInt()
            targetSdk = project.libs.versionOf("targetSdk").toInt()
            versionCode = 110
            versionName = "1.1.0"

        }

        buildTypes {
            debug {
                isMinifyEnabled = false
                isDebuggable = true
                isShrinkResources = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
            release {
                isMinifyEnabled = true
                isDebuggable = false
                isShrinkResources = true
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        project.tasks.withType(KotlinCompile::class.java).configureEach {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
                freeCompilerArgs = listOf("-Xsam-conversions=class")
            }
        }

        buildFeatures {
            buildConfig = true
        }
    }
}
