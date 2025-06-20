plugins {
    alias(libs.plugins.memo.kotlin.jvm.library)
}

dependencies {

    implementation(kotlin("reflect"))

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
