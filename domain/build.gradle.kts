plugins {
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {

    implementation(kotlin("reflect"))

    implementation(project(":core"))

    implementation(libs.javax.inject)

    // coroutines
    implementation(libs.coroutines.core)

    // testing
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.junit)
    testImplementation(libs.test.junit.jupiter)
    testImplementation(libs.test.kotlinx.coroutines)
}

tasks.test {
    useJUnitPlatform()
}
