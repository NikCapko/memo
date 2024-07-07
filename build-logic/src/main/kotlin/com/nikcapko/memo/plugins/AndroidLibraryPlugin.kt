package com.nikcapko.memo.plugins

import com.android.build.api.dsl.LibraryExtension
import com.nikcapko.memo.libraryExtension
import com.nikcapko.memo.libs
import com.nikcapko.memo.pluginOf
import com.nikcapko.memo.versionOf
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.pluginOf("android-library"))
            apply(libs.pluginOf("kotlin-android"))
        }
        libraryExtension.configure(project)
    }

    private fun LibraryExtension.configure(project: Project) {
        compileSdk = project.libs.versionOf("compileSdk").toInt()

        defaultConfig {
            minSdk = project.libs.versionOf("minSdk").toInt()
        }

        buildTypes {
            release {
                isMinifyEnabled = true
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            }
            debug {
                isMinifyEnabled = false
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
    }
}
