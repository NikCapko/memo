package com.nikcapko.memo

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.libs
    get() = this.extensions
        .getByType<VersionCatalogsExtension>()
        .named("libs")

internal fun VersionCatalog.versionOf(name: String) =
    this.findVersion(name).get().requiredVersion

internal fun VersionCatalog.pluginOf(name: String) =
    this.findPlugin(name).get().get().pluginId
