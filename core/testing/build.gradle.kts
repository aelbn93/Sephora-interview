plugins {
    alias(libs.plugins.sephora.android.library)
    alias(libs.plugins.sephora.hilt)
}

android {
    namespace = "com.interview.sephora.core.testing"
}


dependencies {
    api(libs.kotlinx.coroutines.test)
    api(projects.core.common)
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
}