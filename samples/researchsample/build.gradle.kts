plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(31)

    defaultConfig {
        applicationId = "com.samsung.healthcare.kit.sample"
        minSdkVersion(29)
        targetSdkVersion(31)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
            merges += "/META-INF/LICENSE.md"
            merges += "/META-INF/NOTICE.md"
        }
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(project(":kit"))
    implementation(project(":external"))
    implementation(project(":resources:korean"))
    implementation(platform(AppDependencies.FIREBASE_BOM))
    implementation(AppDependencies.authImplLibs)
    implementation(AppDependencies.composeImplLibs)
    implementation(AppDependencies.hiltImplLibs)
    implementation(AppDependencies.healthDataImplLibs)

    debugImplementation(AppDependencies.uiDebugLibs)

    kapt(AppDependencies.hiltKaptLibs)
}
