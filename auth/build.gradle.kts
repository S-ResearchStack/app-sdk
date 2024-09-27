
plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    id("de.mannodermaus.android-junit5")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "researchstack.auth"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
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

    buildFeatures {
        aidl = true
    }

    kapt {
        correctErrorTypes = true
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":backend-integration:interface"))

    implementation(AppDependencies.hiltImplLibs)
    implementation(AppDependencies.androidXImplLibs)
    implementation(AppDependencies.jwtImplLibs)
    implementation(AppDependencies.grpcImplLibs)
    implementation(AppDependencies.AWS_COGNITO_IDENTITY_PROVIDER)

    kapt(AppDependencies.hiltKaptLibs)

    // Unit Test
    testRuntimeOnly(AppDependencies.JUNIT_ENGINE)
    testImplementation(AppDependencies.testImplLibs)
    testImplementation(AppDependencies.COROUTINE_TEST)
}
