plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.eluxwear"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.eluxwear"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // Match the Compose version
    }
}

dependencies {

    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.wear.tooling.preview)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.firebase.firestore)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation (libs.firebase.analytics)
    // Core Jetpack Compose dependencies
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material) // Optional Material design
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Wear OS Compose dependencies
    implementation(libs.androidx.compose.material.v120)
    implementation(libs.androidx.compose.foundation.v120)

    // Lifecycle and Activity Compose support
    implementation(libs.androidx.activity.compose.v172)
    implementation(libs.androidx.lifecycle.runtime.compose)


    //sensor
    implementation (libs.androidx.wear)
    implementation (libs.play.services.wearable.v1700)
    implementation (libs.androidx.core.ktx)
}
