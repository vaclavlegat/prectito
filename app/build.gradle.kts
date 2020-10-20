plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.kotlin.android.extensions")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-android")
}


android {
    compileSdkVersion (Android.compileSdkVersion)
    buildToolsVersion = Android.buildToolsVersion

    defaultConfig {
        applicationId = Android.applicationId
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":core"))
    implementation(project(":booksdb"))

    // core
    implementation (Libs.appcompat)
    implementation (Libs.coreKtx)
    implementation (Libs.kotlinStdlibJdk)
    implementation (Libs.legacySupportV4)

    // di
    implementation (Libs.hilt)
    implementation (Libs.hiltLifecycle)
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    kapt (Libs.hiltCompiler)
    kapt (Libs.hiltAndroidCompiler)

    // images
    implementation (Libs.glide)
    annotationProcessor (Libs.glideCompiler)

    // lifecycle
    implementation (Libs.lifecycleExtensions)
    implementation (Libs.lifecycleLivedataKtx)
    implementation (Libs.lifecycleViewmodelKtx)

    // navigation
    implementation (Libs.navigationFragmentKtx)
    implementation (Libs.navigationUiKtx)

    // network
    implementation (Libs.converterMoshi)
    implementation (Libs.loggingInterceptor)
    implementation (Libs.okhttp)
    implementation (Libs.retrofit)

    // ui
    implementation (Libs.constraintlayout)
    implementation (Libs.material)
    implementation (Libs.recyclerView)
    implementation (Libs.viewPager2)

    implementation (Libs.paging)

    implementation ("com.google.android.gms:play-services-vision:20.1.1")
    implementation ("com.google.mlkit:barcode-scanning:16.0.2")


    implementation ("androidx.preference:preference:1.1.1")

    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    api ("com.google.guava:guava:27.1-jre")

    implementation ("com.google.android.material:material:1.3.0-alpha03")

    implementation (Libs.roomRuntime)
    kapt (Libs.roomCompiler) // For Kotlin use kapt instead of annotationProcessor

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation (Libs.roomKtx)


    // test
    testImplementation (Libs.junit)
    androidTestImplementation (Libs.junitext)
    androidTestImplementation (Libs.espresso)
}
