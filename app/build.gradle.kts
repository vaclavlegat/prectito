plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.kotlin.android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
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

    implementation(project(Modules.core))
    implementation(project(Modules.authors))
    implementation(project(Modules.books))
    implementation(project(Modules.scanner))

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

    // ui
    implementation (Libs.constraintlayout)
    implementation (Libs.material)
    implementation (Libs.recyclerView)
    implementation (Libs.viewPager2)

    implementation (Libs.preference)
    implementation (Libs.retrofit)
    implementation (Libs.roomRuntime)
    api (Libs.guava)

    // test
    testImplementation (Libs.junit)
    androidTestImplementation (Libs.junitext)
    androidTestImplementation (Libs.espresso)
}
