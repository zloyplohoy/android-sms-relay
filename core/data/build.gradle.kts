plugins {
    alias(libs.plugins.smsrelay.android.library)
    alias(libs.plugins.smsrelay.hilt)
}

android {
    namespace = "ag.sokolov.smsrelay.core.data"
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
    api(projects.core.concurrency)
    api(projects.core.datastore)
    api(libs.kotlinx.coroutines.core)
}
