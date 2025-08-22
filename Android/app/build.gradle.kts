plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-parcelize")


    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)


}

android {
    namespace = "com.example.quanlybandienthoai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quanlybandienthoai"
        minSdk = 26//24
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

    viewBinding {
        enable = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
}

dependencies {
    //picasso
    implementation("com.squareup.picasso:picasso:2.8")

    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation("io.coil-kt:coil-gif:2.5.0")
    // Basic libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Compose libraries
    implementation("androidx.compose.ui:ui:1.5.2")
    implementation("androidx.compose.material:material:1.5.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.2")
    implementation("androidx.compose.runtime:runtime:1.5.2")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.database.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.emoji2)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.firebase.messaging.ktx)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Compose tooling
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.2")

    // LiveData support for Compose
    implementation("androidx.compose.runtime:runtime-livedata:1.5.2")

    // ViewModel support for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Animation support
    implementation("androidx.compose.animation:animation:1.6.0")


    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // When using the BoM, you don't specify versions in Firebase library dependencies

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")

    // TODO: Add the dependencies for any other Firebase products you want to use
    // See https://firebase.google.com/docs/android/setup#available-libraries
    // For example, add the dependencies for Firebase Authentication and Cloud Firestore
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")



    implementation(platform("androidx.compose:compose-bom:2025.03.00")) // 🔥 Luôn cập nhật BOM mới nhất!


    implementation("io.coil-kt:coil-compose:2.6.0")
    debugImplementation(libs.androidx.ui.test.manifest) // Nếu dùng Jetpack Compose

    implementation("androidx.compose.material:material-icons-extended:1.6.0")



    implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0")


    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.google.firebase:firebase-auth-ktx")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    //animation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0")

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //momo
    implementation("com.github.momo-wallet:mobile-sdk:1.0.7")
    implementation ("com.android.support:appcompat-v7:28.0.0")

    //revenue
//    implementation(libs.linechart)
//    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
//    implementation("com.github.SmartToolFactory:Compose-Chart:1.0.0-alpha01")
    implementation("co.yml:ycharts:2.1.0")

}

