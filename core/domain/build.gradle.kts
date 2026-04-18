plugins {
    alias(libs.plugins.sephora.android.library)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.interview.sephora.core.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}