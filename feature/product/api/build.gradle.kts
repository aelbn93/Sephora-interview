plugins {
    alias(libs.plugins.sephora.android.feature.api)
}

android {
    namespace = "com.interview.sephora.feature.product.api"
}

dependencies {
    api(projects.core.navigation)
}