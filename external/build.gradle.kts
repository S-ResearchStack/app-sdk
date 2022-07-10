plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("de.mannodermaus.android-junit5")
    id("org.jetbrains.dokka")
    id("io.gitlab.arturbosch.detekt")
    id("jacoco")
}

android {
    compileSdkVersion(31)

    defaultConfig {
        minSdkVersion(29)
        targetSdkVersion(31)

        // 1) Make sure to use the AndroidJUnitRunner, or a subclass of it. This requires a dependency on androidx.test:runner, too!
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 2) Connect JUnit 5 to the runner
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
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
    implementation(AppDependencies.hiltImplLibs)
    implementation(AppDependencies.androidXImplLibs)
    implementation(AppDependencies.ANDROIDX_DATASTORE)
    implementation(AppDependencies.healthDataImplLibs)
    implementation(AppDependencies.httpClientImplLibs)
    implementation(platform(AppDependencies.FIREBASE_BOM))
    implementation(AppDependencies.authImplLibs)

    kapt(AppDependencies.hiltKaptLibs)

    testRuntimeOnly(AppDependencies.testRuntimeLibs)
    testImplementation(AppDependencies.testImplLibs)
    testImplementation(AppDependencies.androidTestImplLibs)
    testImplementation(AppDependencies.coroutineTestImplLibs)

    androidTestRuntimeOnly(AppDependencies.JUNIT_MANNO_RUNNER)
    androidTestImplementation(AppDependencies.androidTestImplLibs)
}

jacoco {
    toolVersion = Versions.JACOCO
}
