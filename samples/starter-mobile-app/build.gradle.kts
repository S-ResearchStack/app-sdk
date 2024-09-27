import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("com.google.protobuf")
    id("io.gitlab.arturbosch.detekt")
    id("de.mannodermaus.android-junit5")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

fun getProperty(key: String): String {
    return gradleLocalProperties(file("."), providers).getProperty(key)
        ?: throw GradleException("you MUST set $key")
}

fun getPropertyOrDefault(key: String, defaultValue: String): String {
    return gradleLocalProperties(file("."), providers).getProperty(key) ?: defaultValue
}

fun getRootProperty(key: String, defaultValue: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(key, defaultValue)
}

android {
    namespace = "researchstack"
    compileSdk = 34

    defaultConfig {
        applicationId = getRootProperty("APPLICATION_ID", "researchstack.starter")
        minSdk = 28
        targetSdk = 34
        versionCode = Versions.APP_VERSION_CODE * 2 - 1
        versionName = Versions.APP_VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty(
            "archivesBaseName",
            "health-research-mobile-v$versionName($versionCode)"
        )

        buildConfigField("String", "SA_CLIENT_ID", getPropertyOrDefault("SA_CLIENT_ID", "\"\""))
        buildConfigField("String", "SA_CLIENT_SECRET", getPropertyOrDefault("SA_CLIENT_SECRET", "\"\""))
        buildConfigField("String", "SERVER_ADDRESS", getProperty("SERVER_ADDRESS"))
        buildConfigField("int", "SERVER_PORT", getProperty("SERVER_PORT"))
        buildConfigField("String", "COGNITO_CLIENT_ID", getPropertyOrDefault("COGNITO_CLIENT_ID", "\"\""))
        buildConfigField("String", "COGNITO_CLIENT_REGION", getPropertyOrDefault("COGNITO_CLIENT_REGION", "\"\""))
        buildConfigField("boolean", "USE_PLAIN_TEXT", getPropertyOrDefault("USE_PLAIN_TEXT", "true"))
        buildConfigField("long", "DATA_SYNC_PERIOD", getPropertyOrDefault("DATA_SYNC_PERIOD", "30"))
        buildConfigField("boolean", "ENABLE_LOGOUT", getPropertyOrDefault("ENABLE_LOGOUT", "false"))
        buildConfigField(
            "boolean",
            "ENABLE_CELLULAR_DATA_TO_SYNC_DATA",
            getPropertyOrDefault("ENABLE_CELLULAR_DATA_TO_SYNC_DATA", "false")
        )
        buildConfigField("boolean", "SHOW_COMPLETE_STUDY", getPropertyOrDefault("SHOW_COMPLETE_STUDY", "true"))
        buildConfigField("boolean", "SUPPORT_IN_CLINIC_MODE", getPropertyOrDefault("SUPPORT_IN_CLINIC_MODE", "true"))
        buildConfigField("String", "FOLDER_NAME", getPropertyOrDefault("FOLDER_NAME", "\"HealthResearch\""))
        buildConfigField("int", "BATCH_HEALTH_DATA_SIZE", getPropertyOrDefault("BATCH_HEALTH_DATA_SIZE", "2000"))
        buildConfigField("int", "NUM_BATCH_HEALTH_DATA", getPropertyOrDefault("NUM_BATCH_HEALTH_DATA", "100"))
        buildConfigField("String", "ADMIN_PASSWORD", getPropertyOrDefault("ADMIN_PASSWORD", "\"\""))
        buildConfigField(
            "boolean",
            "ENABLE_UPLOAD_WEARABLE_DATA_BY_FILE",
            getPropertyOrDefault("ENABLE_UPLOAD_WEARABLE_DATA_BY_FILE", "false")
        )
        buildConfigField("long", "DATA_SPLIT_INTERVAL_IN_MILLIS", getPropertyOrDefault("DATA_SPLIT_INTERVAL_IN_MILLIS", "3600000"))
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        debug {
            enableUnitTestCoverage = true
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
        resources.excludes.add("META-INF/*")
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
    implementation(project(":auth"))
    implementation(project(":common"))
    implementation(project(":backend-integration:interface"))
    implementation(project(":backend-integration:researchstack-adapter"))

    implementation(AppDependencies.composeImplLibsMobile)
    implementation(AppDependencies.hiltImplLibs)
    implementation(AppDependencies.androidXImplLibs)
    implementation(AppDependencies.ANDROIDX_WORK)
    implementation(AppDependencies.jwtImplLibs)
    implementation(AppDependencies.grpcImplLibs)
    implementation(AppDependencies.httpClientImplLibs)
    implementation(AppDependencies.CRON_QUARTZ)
    implementation(AppDependencies.PLAY_SERVICE_WEARBLE)
    implementation(AppDependencies.PLAY_SERVICE_LOCATION)
    implementation(AppDependencies.AWS_COGNITO_IDENTITY_PROVIDER)
    implementation(AppDependencies.APACHE_COMMONS_IO)
    implementation(AppDependencies.JACKSON_DATAFORMAT_CSV)
    implementation(AppDependencies.JACKSON_DATATYPE_JSR310)
    implementation(AppDependencies.JACKSON_MODULE_KOTLIN)
    implementation(AppDependencies.COMMONS_MATH)
    implementation(AppDependencies.GOOGLE_HEALTH_CONNECT)
    kapt(AppDependencies.hiltKaptLibs)
    implementation(AppDependencies.ACCOMPANIST_PAGER)
    implementation(AppDependencies.COIL_COMPOSE)
    implementation(AppDependencies.SIGNATURE)
    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.6")

    // Room
    implementation(AppDependencies.roomLibs)
    kapt(listOf(AppDependencies.ANDROIDX_ROOM_COMPILER))

    // Unit Test
    testRuntimeOnly(AppDependencies.JUNIT_ENGINE)
    testImplementation(AppDependencies.testImplLibs)
    testImplementation(AppDependencies.COROUTINE_TEST)

    // Android Test
    androidTestImplementation(AppDependencies.androidTestImplLibs)
}

jacoco {
    toolVersion = Versions.JACOCO
}
