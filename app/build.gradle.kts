plugins {
    alias(libs.plugins.minux.monitoring.android.application)
    alias(libs.plugins.minux.monitoring.android.application.compose)
    alias(libs.plugins.minux.monitoring.android.dagger)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.minux.monitoring"

    defaultConfig {
        applicationId = "com.minux.monitoring"
        versionCode = 1
        versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(project(":injector"))
    implementation(project(":injector-compose"))

    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":core:base"))

    implementation(project(":feature:auth:api"))
    implementation(project(":feature:auth:impl"))

    implementation(project(":feature:profile:api"))
    implementation(project(":feature:profile:impl"))

    implementation(project(":feature:rigs:api"))
    implementation(project(":feature:rigs:impl"))

    implementation(project(":feature:cryptos:api"))
    implementation(project(":feature:cryptos:impl"))

    implementation(project(":feature:flightsheets:api"))
    implementation(project(":feature:flightsheets:impl"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}