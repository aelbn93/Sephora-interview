
plugins {
    alias(libs.plugins.sephora.android.feature.impl)
    alias(libs.plugins.sephora.android.library.compose)
    alias(libs.plugins.sephora.android.library.jacoco)
}

android {
    namespace = "com.interview.sephora.feature.product.impl"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.product.api)

    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
