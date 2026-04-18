plugins {
    alias(libs.plugins.sephora.android.library)
    alias(libs.plugins.sephora.android.library.compose)
}

android {
    namespace = "com.interview.sephora.core.ui"
}

dependencies {
    api(libs.androidx.metrics)
    api(projects.core.designsystem)
    api(projects.core.model)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
}