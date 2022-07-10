plugins {
    id("com.android.library")
    id("kotlin-android")
    id("de.mannodermaus.android-junit5")
    id("org.jetbrains.dokka")
    id("io.gitlab.arturbosch.detekt")
    id("jacoco")
    id("kotlin-kapt")
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
    implementation(project(":external"))

    implementation(platform(AppDependencies.FIREBASE_BOM))
    implementation(AppDependencies.authImplLibs)
    implementation(AppDependencies.ANDROIDX_DATASTORE)
    implementation(AppDependencies.ANDROIDX_NAVIGATION_COMPOSE)
    implementation(AppDependencies.composeImplLibs)
    implementation(AppDependencies.ACCOMPANIST_PAGER)
    implementation(AppDependencies.SIGNATURE)
    implementation(AppDependencies.coilImplLibs)
    implementation(AppDependencies.GOOGLE_HEALTH_DATA)
    implementation(AppDependencies.SUPPORT_ANNOTATION)
    implementation(AppDependencies.roomLibs)

    kapt(listOf(AppDependencies.ANDROIDX_ROOM_COMPILER))
    implementation(AppDependencies.GSON)
    implementation(AppDependencies.CRON_QUARTZ)

    debugImplementation(AppDependencies.uiDebugLibs)

    testRuntimeOnly(AppDependencies.JUNIT_ENGINE)
    testImplementation(AppDependencies.testImplLibs)

    androidTestRuntimeOnly(AppDependencies.JUNIT_MANNO_RUNNER)
    androidTestImplementation(AppDependencies.androidTestImplLibs)
    androidTestImplementation(AppDependencies.androidUITestImplLibs)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("$buildDir/docs"))
    dokkaSourceSets {
        named("main") {
            noAndroidSdkLink.set(false)
        }
    }
}

jacoco {
    toolVersion = Versions.JACOCO
}
