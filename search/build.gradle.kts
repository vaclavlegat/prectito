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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(Modules.core))
    api(project(Modules.navigation))

    implementation (Libs.hilt)
    implementation (Libs.hiltLifecycle)
    kapt (Libs.hiltCompiler)
    kapt (Libs.hiltAndroidCompiler)

    // core
    implementation (Libs.appcompat)
    implementation (Libs.coreKtx)
    implementation (Libs.kotlinStdlibJdk)
    implementation (Libs.legacySupportV4)

    // lifecycle
    implementation (Libs.lifecycleExtensions)
    implementation (Libs.lifecycleLivedataKtx)
    implementation (Libs.lifecycleViewmodelKtx)

    // navigation
    implementation (Libs.navigationFragmentKtx)
    implementation (Libs.navigationUiKtx)

    // network
    implementation (Libs.retrofit)

    // ui
    implementation (Libs.constraintlayout)
    implementation (Libs.material)
    implementation (Libs.recyclerView)
    implementation (Libs.viewPager2)

    implementation (Libs.paging)

    implementation (Libs.glide)
    kapt (Libs.glideCompiler)

    implementation (Libs.palette)

    // test
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.4")
    testImplementation ("androidx.arch.core:core-testing:2.0.0-alpha1")
    testImplementation("io.mockk:mockk:1.10.2")
    androidTestImplementation("io.mockk:mockk-android:1.10.2")
    testImplementation (Libs.junit)
    androidTestImplementation (Libs.junitext)
    androidTestImplementation (Libs.espresso)

}