plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.baseline.profile)
}

android {
    namespace = "com.prodmobile.template"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.prodmobile.template"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            // in real world we should use env variables
            keyAlias = "key0"
            storeFile = file("keystore.jks")
            storePassword = file("keystore_password").readText().trim()
            keyPassword = file("keystore_password").readText().trim()
        }
    }
    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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

    baselineProfile {
        baselineProfileOutputDir = "../androidMain/generated/baselineProfiles"
        automaticGenerationDuringBuild = true
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices {
            localDevices {
                create("pixel2api34") {
                    // Use device profiles you typically see in Android Studio.
                    device = "Pixel 2"
                    // Use only API levels 27 and higher.
                    apiLevel = 30
                    // To include Google services, use "google".
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
}

dependencies {
    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)

    // kotlinx serialization
    implementation(libs.kotlinx.serialization.json)

    // ktor
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)

    // koin
    implementation(libs.koin.androidx.compose)

    // coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor3)

    // coroutines
    implementation(libs.kotlinx.coroutines.core)

    // paging
    implementation(libs.androidx.paging.compose)

    // datastore
    implementation(libs.androidx.datastore.preferences)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // new splash screen api
    implementation(libs.androidx.core.splashscreen)

    // icons
    implementation(libs.androidx.material.icons.extended)

    // other
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    "baselineProfile"(project(":baselineprofile"))
}