plugins {
    alias(libs.plugins.sephora.jvm.library)
    alias(libs.plugins.sephora.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}