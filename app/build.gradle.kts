plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.metrogrid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.metrogrid"
        minSdk = 28
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    //Timber
    implementation (libs.timber)

    // Coroutines
    implementation (libs.kotlinx.coroutines.android)

    // Coroutine Lifecycle Scopes
    implementation(libs.kotlinx.coroutines.core)

    //Dagger - Hilt
    implementation (libs.hilt.android)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)
    ksp(libs.hilt.android.compiler)
    implementation (libs.androidx.hilt.navigation.compose)

    implementation("androidx.palette:palette:1.0.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.paging:paging-runtime-ktx:3.3.6")
    implementation("androidx.paging:paging-compose:3.3.6")
    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

    implementation(libs.gson)
    implementation(libs.coil.compose)
    implementation( libs.coil.gif   )


}