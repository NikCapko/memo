enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "memo"

includeBuild("build-logic")

include(":app")
include(":data")
include(":domain")
include(":presentation")
folder(
    folder = "core",
    modules = listOf(
        "common",
        "data",
        "test",
        "ui",
    )
)

fun Settings.folder(folder: String, modules: List<String>) {
    modules.forEach { module ->
        include(":$folder-$module")
        project(":$folder-$module").projectDir = File("$folder/$module")
    }
}
