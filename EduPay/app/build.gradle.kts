plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)// Apply Hilt plugin here
    alias(libs.plugins.google.services)
    kotlin("kapt")

}

android {
    namespace = "com.example.edupay"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.edu.edupay"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"

    }
    buildFeatures{
        buildConfig = true
        viewBinding= true
        dataBinding=true
        //compose = true

    }
  /*  composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13" // ✅ Required for Kotlin 2.0+
    }*/

    flavorDimensions += "environment"
    productFlavors {
        create("prod") {
            dimension = "environment"
            //   applicationIdSuffix = ".prod"
            versionNameSuffix = "-prod"

            buildConfigField("String", "BASE_URL_API", "\"https://edfeepay.com/edupay/api/\"")
            buildConfigField("String", "CDN_URL", "\"https://d2xvkgu6hj3rbb.cloudfront.net/\"")
        }

        create("stag") {
            dimension = "environment"
            //   applicationIdSuffix = ".stag"
            versionNameSuffix = "-stag"
            buildConfigField("String", "BASE_URL_API", "\"https://edfeepay.com/edupay/api/\"")
            buildConfigField("String", "CDN_URL", "\"https://d2xvkgu6hj3rbb.cloudfront.net/\"")
        }


    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    implementation("com.hbb20:ccp:2.7.3")
    implementation("io.github.chaosleung:pinview:1.4.4")
    implementation ("nl.dionsegijn:konfetti-xml:2.0.2")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation(libs.kotlin.stdlib)
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.4.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("io.reactivex.rxjava2:rxjava:2.2.9")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.1")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.3.0")

    implementation(libs.imagepicker)
    implementation ("com.google.android.gms:play-services-location:21.0.1")
   // implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.18")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

  //  implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")
  //  implementation("androidx.core:core-ktx:1.16.0")
}