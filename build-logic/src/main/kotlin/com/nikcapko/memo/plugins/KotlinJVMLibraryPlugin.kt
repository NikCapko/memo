package com.nikcapko.memo.plugins

import com.nikcapko.memo.libs
import com.nikcapko.memo.pluginOf
import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinJVMLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.pluginOf("kotlin-jvm"))
            }
        }
    }
}
