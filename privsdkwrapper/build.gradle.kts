import AppDependencies.ANDROIDX_CORE

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "researchstack.privsdkwrapper"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            enableUnitTestCoverage
        }
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
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(mapOf("path" to ":common")))
    implementation(project(":libs:privsdk"))
    implementation(ANDROIDX_CORE)
    implementation(AppDependencies.GSON)

    implementation(AppDependencies.hiltImplLibs)
    kapt(AppDependencies.hiltKaptLibs)
    // Room
    implementation(AppDependencies.roomLibs)
    kapt(listOf(AppDependencies.ANDROIDX_ROOM_COMPILER))
    implementation(AppDependencies.GSON)
    implementation(AppDependencies.ANDROIDX_DATASTORE)
    implementation(AppDependencies.KOTLIN_REFLECT)

    // Unit Test
    testImplementation(AppDependencies.testImplLibs)
}
