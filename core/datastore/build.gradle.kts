plugins {
    alias(libs.plugins.smsrelay.android.library)
    alias(libs.plugins.smsrelay.hilt)
//    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "ag.sokolov.smsrelay.core.datastore.telegramapikey"
    compileSdk = 34

    defaultConfig {
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
}

dependencies {
    api(libs.androidx.datastore.preferences)

    implementation(projects.core.concurrency)
}
