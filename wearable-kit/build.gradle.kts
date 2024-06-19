plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("io.gitlab.arturbosch.detekt")
}

android {
    namespace = "healthstack.wearable.kit"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

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
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.UI
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        disable += "UnusedMaterialScaffoldPaddingParameter"
    }
}

dependencies {
    implementation(project(":common"))

    implementation(AppDependencies.PLAY_SERVICE_WEARABLE)
    implementation(AppDependencies.androidXImplLibsWearable)
    implementation(AppDependencies.composeImplLibsWearable)
    implementation(AppDependencies.hiltImplLibs)
    implementation(AppDependencies.GSON)
    implementation(AppDependencies.WEAR_APPCOMPAT)
    implementation(AppDependencies.WEAR_MATERIAL)
    implementation(AppDependencies.WEAR_CONST_LAYOUT)
    implementation(AppDependencies.WEAR_TEST_MONITOR)
    debugImplementation(AppDependencies.uiDebugLibsWearable)
    kapt(AppDependencies.hiltKaptLibs)

    // Room
    implementation(AppDependencies.roomLibs)
    kapt(listOf(AppDependencies.ANDROIDX_ROOM_COMPILER))

    // Unit Test
    testImplementation(AppDependencies.JUNIT)
    testImplementation(AppDependencies.testImplLibs)
}
