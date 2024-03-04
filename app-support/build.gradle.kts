plugins {
    id("com.android.library")
    id("kotlin-android")
    id("de.mannodermaus.android-junit5")
    id("org.jetbrains.dokka")
    id("io.gitlab.arturbosch.detekt")
    id("kotlin-kapt")
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 29
        targetSdk = 34

        // 1) Make sure to use the AndroidJUnitRunner, or a subclass of it. This requires a dependency on androidx.test:runner, too!
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 2) Connect JUnit 5 to the runner
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
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
    buildToolsVersion = "30.0.3"
}

dependencies {
    implementation(project(":kit"))
    implementation(project(":healthdata-link:interface"))
    implementation(project(":backend-integration:interface"))

    implementation(AppDependencies.androidXImplLibs)
    implementation(platform(AppDependencies.FIREBASE_BOM))
    implementation(AppDependencies.authImplLibs)
    implementation(AppDependencies.ANDROIDX_NAVIGATION_COMPOSE)
    implementation(AppDependencies.composeImplLibs)
    implementation(AppDependencies.ACCOMPANIST_PAGER)
    implementation(AppDependencies.SUPPORT_ANNOTATION)
    implementation(AppDependencies.ANDROIDX_DATASTORE)
    implementation(AppDependencies.roomLibs)
    implementation(AppDependencies.GSON)
    implementation(AppDependencies.CRON_QUARTZ)
    implementation(AppDependencies.GUAVA)
    kapt(listOf(AppDependencies.ANDROIDX_ROOM_COMPILER))

    debugImplementation(AppDependencies.uiDebugLibs)

    testRuntimeOnly(AppDependencies.JUNIT_ENGINE)
    testImplementation(AppDependencies.testImplLibs)
    testImplementation(AppDependencies.coroutineTestImplLibs)

    androidTestRuntimeOnly(AppDependencies.JUNIT_MANNO_RUNNER)
    androidTestImplementation(AppDependencies.androidTestImplLibs)
    androidTestImplementation(AppDependencies.androidUITestImplLibs)
    androidTestImplementation(AppDependencies.ANDROIDX_WORK_TESTING)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("$buildDir/docs"))
    dokkaSourceSets {
        named("main") {
            noAndroidSdkLink.set(false)
        }
    }
}
