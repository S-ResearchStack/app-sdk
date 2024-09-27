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

fun getProperty(key: String): String {
    return gradleLocalProperties(file("."), providers).getProperty(key)
        ?: throw GradleException("you MUST set $key")
}

fun getPropertyOrDefault(key: String, defaultValue: String): String {
    return gradleLocalProperties(file("."), providers).getProperty(key) ?: defaultValue
}

android {
    namespace = "researchstack.wearable.standalone"
    compileSdk = 34

    defaultConfig {
        applicationId = getRootProperty("APPLICATION_ID", "researchstack.wearable.standalone")
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "SERVER_ADDRESS", getProperty("SERVER_ADDRESS"))
        buildConfigField("int", "SERVER_PORT", getProperty("SERVER_PORT"))
        buildConfigField("boolean", "USE_PLAIN_TEXT", getProperty("USE_PLAIN_TEXT"))
        buildConfigField("String", "STUDY_ID", getProperty("STUDY_ID"))
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
    implementation(project(mapOf("path" to ":auth")))
    implementation(project(mapOf("path" to ":common")))
    implementation(project(mapOf("path" to ":privsdkwrapper")))
    implementation(project(":libs:privsdk"))
    implementation(project(":backend-integration:interface"))
    implementation(project(":backend-integration:researchstack-adapter"))
    implementation(AppDependencies.PLAY_SERVICE_WEARBLE)
    implementation(AppDependencies.androidXImplLibsWearable)
    implementation(AppDependencies.composeImplLibsWearable)
    implementation(AppDependencies.hiltImplLibs)
    implementation(AppDependencies.grpcImplLibs)
    implementation(AppDependencies.GSON)
    implementation(AppDependencies.ANDROIDX_COMPAT)
    implementation(AppDependencies.MATERIAL)
    implementation(AppDependencies.COMPOSE_MATERIAL_WEAR)
    implementation(AppDependencies.COMPOSE_FOUNDATION)
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:${Versions.KOTLIN}"))
    debugImplementation(AppDependencies.uiDebugLibsWearable)
    kapt(AppDependencies.hiltKaptLibs)
    implementation(AppDependencies.KOTLIN_REFLECT)
    implementation(AppDependencies.APACHE_COMMONS_IO)
    implementation(AppDependencies.JACKSON_DATAFORMAT_CSV)
    implementation(AppDependencies.JACKSON_DATATYPE_JSR310)
    implementation(AppDependencies.JACKSON_MODULE_KOTLIN)

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
