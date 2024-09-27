import AppDependencies.ANDROIDX_CORE
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("de.mannodermaus.android-junit5")
}

fun getPropertyOrDefault(key: String, defaultValue: String): String {
    return gradleLocalProperties(file("."), providers).getProperty(key) ?: defaultValue
}

android {
    namespace = "researchstack.common"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    val versionName = Versions.APP_VERSION_NAME
    val versionCode = Versions.APP_VERSION_CODE * 2 - 1
    buildTypes.all {
        buildConfigField("String", "VERSION_NAME", "\"$versionName\"")
        buildConfigField("int", "VERSION_CODE", "$versionCode")
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

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":backend-integration:interface"))

    implementation(ANDROIDX_CORE)
    implementation(AppDependencies.GSON)
    // Room
    implementation(AppDependencies.roomLibs)
    kapt(listOf(AppDependencies.ANDROIDX_ROOM_COMPILER))
    implementation(AppDependencies.GSON)
    implementation(AppDependencies.ANDROIDX_DATASTORE)
    implementation(AppDependencies.KOTLIN_REFLECT)

    // Unit Test
    testRuntimeOnly(AppDependencies.JUNIT_ENGINE)
    testImplementation(AppDependencies.testImplLibs)
    testImplementation(AppDependencies.COROUTINE_TEST)
}
