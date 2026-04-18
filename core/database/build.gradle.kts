plugins {
    alias(libs.plugins.sephora.android.library)
    alias(libs.plugins.sephora.android.room)
    alias(libs.plugins.sephora.hilt)
}


android {
    namespace = "com.interview.sephora.core.database"
}

dependencies {
    api(projects.core.model)

    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}