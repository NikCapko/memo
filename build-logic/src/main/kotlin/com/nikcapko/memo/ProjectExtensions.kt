package com.nikcapko.memo

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

internal val Project.applicationExtension
    get() = the<ApplicationExtension>()

internal val Project.libraryExtension
    get() = the<LibraryExtension>()
