plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.dokka")
    id("de.mannodermaus.android-junit5")
    id("io.gitlab.arturbosch.detekt")
    id("kotlin-kapt")
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 29
        targetSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
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
    implementation(project(":healthdata-link:interface"))
    implementation(AppDependencies.GOOGLE_HEALTH_CONNECT)
    implementation(AppDependencies.activityImplLibs)
    testRuntimeOnly(AppDependencies.JUNIT_ENGINE)
    testImplementation(AppDependencies.testImplLibs)
    testImplementation(AppDependencies.coroutineTestImplLibs)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("$buildDir/docs"))
    dokkaSourceSets {
        named("main") {
            noAndroidSdkLink.set(false)
        }
    }
}
