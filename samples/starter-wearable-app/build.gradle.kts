import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("io.gitlab.arturbosch.detekt")
}

fun getRootProperty(key: String, defaultValue: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key, defaultValue)
}

fun getPropertyOrDefault(key: String, defaultValue: String): String {
    return gradleLocalProperties(file("."), providers).getProperty(key) ?: defaultValue
}

android {
    namespace = "researchstack"
    compileSdk = 34

    defaultConfig {
        applicationId = getRootProperty("APPLICATION_ID", "researchstack.starter")
        minSdk = 30
        targetSdk = 34
        versionCode = Versions.APP_VERSION_CODE * 2
        versionName = Versions.APP_VERSION_NAME
        vectorDrawables {
            useSupportLibrary = true
        }

        setProperty(
            "archivesBaseName",
            "health-research-wearable-v$versionName($versionCode)"
        )

        buildConfigField("int", "COLLECT_PASSIVE_DATA_PERIOD", getPropertyOrDefault("COLLECT_PASSIVE_DATA_PERIOD", "0"))
        buildConfigField(
            "int",
            "COLLECT_PASSIVE_DATA_DURATION",
            getPropertyOrDefault("COLLECT_PASSIVE_DATA_DURATION", "0")
        )
        buildConfigField(
            "int",
            "PASSIVE_DATA_INSERT_INTERVAL_IN_SECONDS",
            getPropertyOrDefault("PASSIVE_DATA_INSERT_INTERVAL_IN_SECONDS", "600")
        )
        buildConfigField(
            "boolean",
            "ENABLE_INSTANT_SYNC_BUTTON",
            getPropertyOrDefault("ENABLE_INSTANT_SYNC_BUTTON", "false")
        )
        buildConfigField("long", "DATA_SPLIT_INTERVAL_IN_MILLIS", getPropertyOrDefault("DATA_SPLIT_INTERVAL_IN_MILLIS", "3600000"))
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
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.COMPILER
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(AppDependencies.PLAY_SERVICE_WEARBLE)
    implementation(AppDependencies.androidXImplLibsWearable)
    implementation(AppDependencies.composeImplLibsWearable)
    implementation(AppDependencies.hiltImplLibs)
    implementation(AppDependencies.GSON)
    implementation(AppDependencies.ANDROIDX_COMPAT)
    implementation(AppDependencies.MATERIAL)
    implementation(AppDependencies.COMPOSE_MATERIAL_WEAR)
    implementation(project(mapOf("path" to ":common")))
    implementation(project(mapOf("path" to ":privsdkwrapper")))
    implementation(project(":libs:privsdk"))
    implementation(AppDependencies.COMPOSE_FOUNDATION)
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:${Versions.KOTLIN}"))
    debugImplementation(AppDependencies.uiDebugLibsWearable)
    kapt(AppDependencies.hiltKaptLibs)
    implementation(AppDependencies.KOTLIN_REFLECT)

    // Room
    implementation(AppDependencies.roomLibs)
    kapt(listOf(AppDependencies.ANDROIDX_ROOM_COMPILER))
    implementation(AppDependencies.ANDROIDX_PAGING)
    implementation(AppDependencies.ANDROIDX_ROOM_PAGING)

    // Unit Test
    testRuntimeOnly(AppDependencies.JUNIT_ENGINE)
    testImplementation(AppDependencies.testImplLibs)
    testImplementation(AppDependencies.COROUTINE_TEST)
}
