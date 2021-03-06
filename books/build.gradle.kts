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
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
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

    implementation (Libs.timber)
    implementation (Libs.palette)

    testImplementation ("io.appium:java-client:7.3.0")
    
    // test
    testImplementation (Libs.junit)
    androidTestImplementation (Libs.junitext)
    androidTestImplementation (Libs.espresso)

    implementation (Libs.insetter)
    implementation (Libs.dexter)

}