import com.interview.sephora.SephoraBuildType

plugins {
    alias(libs.plugins.sephora.android.application)
    alias(libs.plugins.sephora.android.application.compose)
    alias(libs.plugins.sephora.hilt)
}

android {

    defaultConfig {
        applicationId = "com.interview.sephora"
        versionCode = 1
        versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = SephoraBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = providers.gradleProperty("minifyWithR8")
                .map(String::toBooleanStrict).getOrElse(true)
            applicationIdSuffix = SephoraBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    namespace = "com.interview.sephora"

}

dependencies {

    implementation(projects.feature.product.api)
    implementation(projects.feature.product.impl)

    implementation(projects.core.navigation)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.kt)

    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)
}