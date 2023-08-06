plugins {
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {

    implementation(project(":core"))
    implementation(libs.javax.inject)

    // coroutines
    implementation(libs.coroutines.core)
}
