plugins {
    alias(libs.plugins.smsrelay.jvm.library)
    alias(libs.plugins.smsrelay.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
