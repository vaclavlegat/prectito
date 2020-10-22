plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.kotlin.android.extensions")
    kotlin("kapt")
    id("kotlin-android")
}

android {
    compileSdkVersion (Android.compileSdkVersion)
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        minSdkVersion (Android.minSdkVersion)
        targetSdkVersion (Android.targetSdkVersion)
        versionCode  = Android.versionCode
        versionName = Android.versionName

        testInstrumentationRunner  = Android.testRunner
    }

    buildTypes {
        getByName ("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(Modules.core))
    implementation(Libs.jsoup)
    implementation (Libs.appcompat)
    implementation (Libs.coreKtx)
    implementation (Libs.kotlinStdlibJdk)
    testImplementation (Libs.junit)
    androidTestImplementation (Libs.junitext)
    androidTestImplementation (Libs.espresso)

}