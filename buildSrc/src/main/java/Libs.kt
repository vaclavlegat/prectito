object Libs {
    
    // core
    val kotlinStdlibJdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinStdlibJdkVersion}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompatVersion}"
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
    val legacySupportV4 = "androidx.legacy:legacy-support-v4:${Versions.legacySupportV4Version}"

    // di
    val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"

    // images
    val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"
    
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
    
    // ui
    val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayoutVersion}"
    val material = "com.google.android.material:material:${Versions.materialVersion}"
    val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2Version}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerViewVersion}"

    // test
    val junit = "junit:junit:${Versions.junitVersion}"
    val junitext = "androidx.test.ext:junit:${Versions.junitextVersion}"
    val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
  
}
