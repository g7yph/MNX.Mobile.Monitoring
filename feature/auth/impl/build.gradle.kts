plugins {
    alias(libs.plugins.minux.monitoring.android.feature)
    alias(libs.plugins.minux.monitoring.android.library.compose)
}

android {
    namespace = "com.minux.monitoring.feature.auth.impl"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(project(":feature:auth:api"))
    implementation(project(":core:network"))

    testApi(libs.kotlinx.coroutines.test)
    testApi(libs.turbine)

    androidTestApi(libs.kotlinx.coroutines.test)
    androidTestApi(libs.turbine)

    testImplementation(libs.bundles.test)
    testImplementation(libs.retrofit.mock)
    androidTestImplementation(libs.bundles.android.test)
}