object Libs {

    // core
    val kotlinStdlibJdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinStdlibJdkVersion}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompatVersion}"
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
    val legacySupportV4 = "androidx.legacy:legacy-support-v4:${Versions.legacySupportV4Version}"

    // di
    val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    val hiltLifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01"
    val hiltCompiler = "androidx.hilt:hilt-compiler:1.0.0-alpha01"
    val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"

    // room
    val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"

    // images
    val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"
    val glideTransformation = "jp.wasabeef:glide-transformations:${Versions.glideTransformationVersion}"

    // lifecycle
    val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleVersion}"
    val lifecycleViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"
    val lifecycleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"

    // navigation
    val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navVersion}"
    val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navVersion}"

    // network
    val converterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.converterMoshiVersion}"
    val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpVersion}"
    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttpVersion}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"

    // paging
    val paging = "androidx.paging:paging-runtime:${Versions.paging_version}"

    // ui
    val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayoutVersion}"
    val material = "com.google.android.material:material:${Versions.materialVersion}"
    val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2Version}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerViewVersion}"

    // test
    val junit = "junit:junit:${Versions.junitVersion}"
    val junitext = "androidx.test.ext:junit:${Versions.junitextVersion}"
    val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"

    val jsoup = "org.jsoup:jsoup:${Versions.jsoupVersion}"

    val playServicesVision = "com.google.android.gms:play-services-vision:${Versions.playServicesVisionVersion}"
    val barcodeScanning = "com.google.mlkit:barcode-scanning:${Versions.barcodeScanningVersion}"

    val preference = "androidx.preference:preference:${Versions.preferenceVersion}"

    val converterScalars = "com.squareup.retrofit2:converter-scalars:${Versions.converterScalarsVersion}"

    val guava = "com.google.guava:guava:${Versions.guavaVersion}"

    val moshiAdapters = "com.squareup.moshi:moshi-adapters:${Versions.moshiVersion}"
    val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshiVersion}"
    val moshiKotlinCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiVersion}"

    val palette = "androidx.palette:palette:${Versions.paletteVersion}"

    val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"

    val insetter = "dev.chrisbanes.insetter:insetter:${Versions.insetterVersion}"
    val dexter = "com.karumi:dexter:${Versions.dexterVersion}"
}
