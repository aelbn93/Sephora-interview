plugins {
    alias(libs.plugins.sephora.android.library)

    alias(libs.plugins.sephora.hilt)
    id("kotlinx-serialization")
}
android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.interview.sephora.core.network"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(libs.kotlinx.datetime)
    api(projects.core.common)
    api(projects.core.model)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)

    testImplementation(libs.kotlinx.coroutines.test)
}