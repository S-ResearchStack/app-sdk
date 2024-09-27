import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    id("com.google.protobuf")
    id("de.mannodermaus.android-junit5")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "researchstack.backend.integration"
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

    sourceSets.getByName("main") {
        proto { srcDir("../../proto") }
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
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

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Versions.GRPC.PROTOC}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${Versions.GRPC.GRPC}"
        }
        id("grpckt") {
            artifact =
                "io.grpc:protoc-gen-grpc-kotlin:${Versions.GRPC.GRPCKT}:jdk${Versions.GRPC.JDK}@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                id("java")
            }
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}
