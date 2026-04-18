plugins {
    alias(libs.plugins.sephora.android.library)
    alias(libs.plugins.sephora.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.interview.sephora.core.data"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.database)

    implementation(libs.androidx.paging.runtime)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(projects.core.testing)
}
