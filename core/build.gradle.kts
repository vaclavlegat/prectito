plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.kotlin.android.extensions")
    id("dagger.hilt.android.plugin")
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

    buildFeatures {
        viewBinding = true
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.paging)
    implementation(Libs.jsoup)
    implementation(Libs.okhttp)
    implementation(Libs.loggingInterceptor)
    implementation(Libs.converterScalars)
    implementation(Libs.converterMoshi)
    implementation (Libs.appcompat)
    implementation (Libs.coreKtx)
    implementation (Libs.kotlinStdlibJdk)
    implementation (Libs.hilt)
    implementation (Libs.hiltLifecycle)
    kapt (Libs.hiltCompiler)
    kapt (Libs.hiltAndroidCompiler)
    implementation (Libs.glide)
    annotationProcessor (Libs.glideCompiler)
    implementation (Libs.roomRuntime)
    kapt (Libs.roomCompiler) // For Kotlin use kapt instead of annotationProcessor
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation (Libs.roomKtx)
    implementation (Libs.moshiAdapters)
    implementation (Libs.moshiKotlin)
    testImplementation (Libs.junit)
    androidTestImplementation (Libs.junitext)
    androidTestImplementation (Libs.espresso)

}