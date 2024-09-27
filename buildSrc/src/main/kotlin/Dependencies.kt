import org.gradle.api.artifacts.dsl.DependencyHandler

object Versions {
    const val APP_VERSION_NAME = "1.0.11"
    const val APP_VERSION_CODE = 15

    const val KOTLIN = "1.9.23"

    object GRPC {
        const val PROTOBUF = "0.9.4"
        const val GRPC = "1.66.0"
        const val GRPCKT = "1.4.1"
        const val PROTOC = "4.28.0"
        const val JDK = "8"
        const val COMMON = "2.44.0"
    }

    object Hilt {
        const val DAGGER = "2.49"
        const val ANDROIDX = "1.2.0"
    }

    object Google {
        const val HEALTH_DATA = "1.0.0-alpha01"
        const val GMS = "4.4.2"
        const val FIREBASE_BOM = "30.1.0"
        const val PLAY_SERVICE_AUTH = "20.2.0"
        const val PLAY_SERVICE_WEARABLE = "18.0.0"
        const val PLAY_SERVICE_LOCATION = "18.0.0"
    }

    object AndroidX {
        const val CORE_KTX = "1.13.1"
        const val COMPAT = "1.6.1"
        const val WORK = "2.9.1"
        const val CONCURRENT = "1.1.0"
        const val CUSTOM_VIEW = "1.1.0"
        const val CUSTOM_VIEW_CONTAINER = "1.0.0"
        const val DATASTORE = "1.1.1"
        const val NAVIGATION_COMPOSE = "2.7.7"
        const val ROOM = "2.6.1"
        const val SPLASH_SCREEN = "1.0.1"
        const val PAGING = "3.1.1"
    }

    object AWS {
        const val COGNITO = "1.2.56"
    }

    const val APACHE_COMMONS_IO = "2.16.1"
    const val JAVA_JWT = "4.4.0"
    const val RETROFIT = "2.11.0"
    const val OKHTTP = "4.11.0"
    const val GSON = "2.10.1"
    const val CRON_QUARTZ = "2.3.2"
    const val JACKSON = "2.14.2"

    const val DOKKA = "1.6.21"

    // TEST
    const val MOCKK = "1.13.11"
    const val MOCKITO = "5.13.0"
    const val RUNNER = "1.6.0"
    const val JACOCO = "0.8.12"
    const val COROUTINE = "1.8.1"

    object JUnit {
        const val EXT = "1.2.1"
        const val CORE = "5.11.0"
        const val TEST = "4.13.2"
        const val PLUGIN = "1.11.0.0"
        const val MANNODERMAUS = "1.5.0"
    }

    const val ESPRESSO = "3.6.1"
    const val DETEKT = "1.23.6"
    const val KTLINT = "12.1.0"

    // UI
    object Compose {
        const val COMPILER = "1.5.13"
        const val UI = "1.6.8"
        const val ACTIVITY = "1.9.1"
        const val WEARABLE = "1.3.1"
        const val LIFECYCLE_VIEWMODEL_COMPOSE = "2.7.0"
    }

    const val COIL = "2.6.0"
    const val MATERIAL_UI = "1.12.0"
    const val ACCOMPANIST_PAGER = "0.34.0"
    const val SIGNATURE_PAD = "0.1.2"
    const val ROOT_COVERAGE = "1.8.0"
    const val GOOGLE_HEALTH_CONNECT = "1.0.0-alpha11"
    const val COMMONS_MATH = "3.3"
}

object AppDependencies {
    // App
    const val ANDROIDX_CORE = "androidx.core:core-ktx:${Versions.AndroidX.CORE_KTX}"
    const val ANDROIDX_COMPAT = "androidx.appcompat:appcompat:${Versions.AndroidX.COMPAT}"
    const val ANDROIDX_WORK = "androidx.work:work-runtime-ktx:${Versions.AndroidX.WORK}"
    const val ANDROIDX_CONCURRENT =
        "androidx.concurrent:concurrent-futures-ktx:${Versions.AndroidX.CONCURRENT}"
    const val ANDROIDX_DATASTORE =
        "androidx.datastore:datastore-preferences:${Versions.AndroidX.DATASTORE}"
    const val ANDROIDX_SPLASH_SCREEN =
        "androidx.core:core-splashscreen:${Versions.AndroidX.SPLASH_SCREEN}"
    const val ANDROIDX_NAVIGATION_COMPOSE =
        "androidx.navigation:navigation-compose:${Versions.AndroidX.NAVIGATION_COMPOSE}"
    const val ANDROIDX_ROOM = "androidx.room:room-runtime:${Versions.AndroidX.ROOM}"
    const val ANDROIDX_ROOM_COMPILER = "androidx.room:room-compiler:${Versions.AndroidX.ROOM}"
    const val ANDROIDX_ROOM_KTX = "androidx.room:room-ktx:${Versions.AndroidX.ROOM}"
    const val ANDROIDX_ROOM_PAGING = "androidx.room:room-paging:${Versions.AndroidX.ROOM}"
    const val ANDROIDX_PAGING = "androidx.paging:paging-runtime:${Versions.AndroidX.PAGING}"
    const val APACHE_COMMONS_IO = "commons-io:commons-io:${Versions.APACHE_COMMONS_IO}"

    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val CRON_QUARTZ = "org.quartz-scheduler:quartz:${Versions.CRON_QUARTZ}"

    const val HILT_DAGGER = "com.google.dagger:hilt-android:${Versions.Hilt.DAGGER}"
    const val HILT_DAGGER_COMPILER = "com.google.dagger:hilt-compiler:${Versions.Hilt.DAGGER}"
    const val HILT_ANDROIDX = "androidx.hilt:hilt-common:${Versions.Hilt.ANDROIDX}"
    const val HILT_ANDROIDX_WORK = "androidx.hilt:hilt-work:${Versions.Hilt.ANDROIDX}"
    const val HILT_ANDORIDX_COMPILER = "androidx.hilt:hilt-compiler:${Versions.Hilt.ANDROIDX}"
    const val HILT_COMPOSE = "androidx.hilt:hilt-navigation-compose:${Versions.Hilt.ANDROIDX}"

    const val GOOGLE_HEALTH_DATA =
        "com.google.android.libraries.healthdata:health-data-api:${Versions.Google.HEALTH_DATA}"
    const val GOOGLE_HEALTH_CONNECT= "androidx.health.connect:connect-client:${Versions.GOOGLE_HEALTH_CONNECT}"

    const val JACKSON_DATAFORMAT_CSV =
        "com.fasterxml.jackson.dataformat:jackson-dataformat-csv:${Versions.JACKSON}"
    const val JACKSON_DATATYPE_JSR310 =
        "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.JACKSON}"
    const val JACKSON_MODULE_KOTLIN =
        "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.JACKSON}"
    const val JAVA_JWT = "com.auth0:java-jwt:${Versions.JAVA_JWT}"

    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"

    const val AWS_COGNITO_IDENTITY_PROVIDER =
        "aws.sdk.kotlin:cognitoidentityprovider:${Versions.AWS.COGNITO}"

    const val COMMONS_MATH = "org.apache.commons:commons-math3:${Versions.COMMONS_MATH}"

    val healthDataImplLibs =
        arrayListOf<String>().apply {
            add(ANDROIDX_WORK)
            add(GOOGLE_HEALTH_DATA)
        }

    val jwtImplLibs =
        arrayListOf<String>().apply {
            add(JAVA_JWT)
        }

    val androidXImplLibs =
        arrayListOf<String>().apply {
            add(ANDROIDX_CORE)
            add(ANDROIDX_DATASTORE)
            add(ANDROIDX_SPLASH_SCREEN)
        }

    val androidXImplLibsWearable =
        arrayListOf<String>().apply {
            add(ANDROIDX_WORK)
            add(ANDROIDX_CORE)
            add(ANDROIDX_DATASTORE)
            add(ANDROIDX_COMPAT)
        }

    val hiltImplLibs =
        arrayListOf<String>().apply {
            add(HILT_ANDROIDX)
            add(HILT_ANDROIDX_WORK)
            add(HILT_DAGGER)
            add(HILT_COMPOSE)
        }

    val hiltKaptLibs =
        arrayListOf<String>().apply {
            add(HILT_DAGGER_COMPILER)
            add(HILT_ANDORIDX_COMPILER)
        }

    val roomLibs =
        arrayListOf<String>().apply {
            add(ANDROIDX_ROOM)
            add(ANDROIDX_ROOM_KTX)
            add(ANDROIDX_ROOM_PAGING)
            add(ANDROIDX_PAGING)
        }

    // Wearable
    const val PLAY_SERVICE_WEARBLE =
        "com.google.android.gms:play-services-wearable:${Versions.Google.PLAY_SERVICE_WEARABLE}"

    // Location
    const val PLAY_SERVICE_LOCATION =
        "com.google.android.gms:play-services-location:${Versions.Google.PLAY_SERVICE_LOCATION}"

    // Auth
    const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.Google.FIREBASE_BOM}"
    const val FIREBASE_AUTH = "com.google.firebase:firebase-auth-ktx"
    const val PLAYSERVICE_AUTH =
        "com.google.android.gms:play-services-auth:${Versions.Google.PLAY_SERVICE_AUTH}"

    val authImplLibs =
        arrayListOf<String>().apply {
            add(FIREBASE_AUTH)
            add(PLAYSERVICE_AUTH)
        }

    // HTTP
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val OKHTTP_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"

    val httpClientImplLibs =
        arrayListOf<String>().apply {
            add(RETROFIT)
            add(RETROFIT_GSON)
            add(OKHTTP)
            add(OKHTTP_INTERCEPTOR)
        }

    // GRPC
    const val GRPC_OK_HTTP = "io.grpc:grpc-okhttp:${Versions.GRPC.GRPC}"
    const val GRPC_PROTOBUF = "io.grpc:grpc-protobuf:${Versions.GRPC.GRPC}"
    const val GRPC_INPROCESS = "io.grpc:grpc-inprocess:${Versions.GRPC.GRPC}"
    const val GRPC_KOTLIN_STUB = "io.grpc:grpc-kotlin-stub:${Versions.GRPC.GRPCKT}"
    const val GRPC_PROTOBUF_JAVA_UTIL =
        "com.google.protobuf:protobuf-java-util:${Versions.GRPC.PROTOC}"
    const val GRPC_PROTO_COMMON =
        "com.google.api.grpc:proto-google-common-protos:${Versions.GRPC.COMMON}"

    // Test
    const val RUNNER = "androidx.test:runner:${Versions.RUNNER}"
    const val JUNIT = "junit:junit:${Versions.JUnit.TEST}"
    const val JUNIT_EXT = "androidx.test.ext:junit:${Versions.JUnit.EXT}"
    const val JUNIT_API = "org.junit.jupiter:junit-jupiter-api:${Versions.JUnit.CORE}"
    const val JUNIT_PARAMS = "org.junit.jupiter:junit-jupiter-params:${Versions.JUnit.CORE}"
    const val JUNIT_ENGINE = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUnit.CORE}"
    const val JUNIT_ENGINE_VINTAGE = "org.junit.vintage:junit-vintage-engine:${Versions.JUnit.CORE}"
    const val JUNIT_JUPITER = "org.junit.jupiter:junit-jupiter:${Versions.JUnit.CORE}"
    const val JUNIT_MANNO_CORE =
        "de.mannodermaus.junit5:android-test-core:${Versions.JUnit.MANNODERMAUS}"
    const val JUNIT_MANNO_RUNNER =
        "de.mannodermaus.junit5:android-test-runner:${Versions.JUnit.MANNODERMAUS}"
    const val COMPOSE_JUNIT = "androidx.compose.ui:ui-test-junit4:${Versions.Compose.UI}"
    const val COMPOSE_UI_TEST = "androidx.compose.ui:ui-test-manifest:${Versions.Compose.UI}"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
    const val MOCKK_ANDROID = "io.mockk:mockk-android:${Versions.MOCKK}"
    const val MOCKITO = "org.mockito:mockito-core:${Versions.MOCKITO}"
    const val COROUTINE_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINE}"
    const val COROUTINE_GUAVA =
        "org.jetbrains.kotlinx:kotlinx-coroutines-guava:${Versions.COROUTINE}"

    val grpcImplLibs = arrayListOf<String>().apply {
        add(GRPC_OK_HTTP)
        add(GRPC_PROTOBUF)
        add(GRPC_INPROCESS)
        add(GRPC_KOTLIN_STUB)
        add(GRPC_PROTOBUF_JAVA_UTIL)
        add(GRPC_PROTO_COMMON)
    }

    val testRuntimeLibs =
        arrayListOf<String>().apply {
            add(JUNIT_ENGINE)
            add(JUNIT_ENGINE_VINTAGE)
        }

    val testImplLibs =
        arrayListOf<String>().apply {
            add(MOCKK)
            add(MOCKITO)
            add(JUNIT)
            add(JUNIT_API)
            add(JUNIT_PARAMS)
        }

    val coroutineTestImplLibs =
        arrayListOf<String>().apply {
            add(COROUTINE_TEST)
            add(COROUTINE_GUAVA)
        }

    val androidUITestImplLibs =
        arrayListOf<String>().apply {
            add(COMPOSE_JUNIT)
            add(COMPOSE_UI_TEST)
        }

    val androidTestImplLibs =
        arrayListOf<String>().apply {
            add(RUNNER)
            add(JUNIT_API)
            add(JUNIT_JUPITER)
            add(JUNIT_EXT)
            add(MOCKK_ANDROID)
            add(JUNIT_PARAMS)
            add(JUNIT_MANNO_CORE)
            add(ESPRESSO)
        }

    // UI
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL_UI}"
    const val COMPOSE_UI = "androidx.compose.ui:ui:${Versions.Compose.UI}"
    const val COMPOSE_RUNTIME = "androidx.compose.runtime:runtime-livedata:${Versions.Compose.UI}"
    const val COMPOSE_FOUNDATION = "androidx.compose.foundation:foundation:${Versions.Compose.UI}"
    const val COMPOSE_MATERIAL_ANDROID = "androidx.compose.material:material:${Versions.Compose.UI}"
    const val COMPOSE_MATERIAL_ANDROID_ICON =
        "androidx.compose.material:material-icons-core:${Versions.Compose.UI}"
    const val COMPOSE_MATERIAL_ANDROID_ICON_EXT =
        "androidx.compose.material:material-icons-extended:${Versions.Compose.UI}"
    const val COMPOSE_ACTIVITY = "androidx.activity:activity-compose:${Versions.Compose.ACTIVITY}"
    const val ACCOMPANIST_PAGER =
        "com.google.accompanist:accompanist-pager:${Versions.ACCOMPANIST_PAGER}"
    const val SIGNATURE = "se.warting.signature:signature-pad:${Versions.SIGNATURE_PAD}"
    const val COIL_COMPOSE = "io.coil-kt:coil-compose:${Versions.COIL}"
    const val COMPOSE_MATERIAL_WEAR =
        "androidx.wear.compose:compose-material:${Versions.Compose.WEARABLE}"
    const val COMPOSE_FOUNDATION_WEAR =
        "androidx.wear.compose:compose-foundation:${Versions.Compose.WEARABLE}"
    const val COMPOSE_LIFECYCLE_VIEWMODEL =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Compose.LIFECYCLE_VIEWMODEL_COMPOSE}"

    val composeImplLibsMobile =
        arrayListOf<String>().apply {
            add(MATERIAL)
            add(COMPOSE_UI)
            add(COMPOSE_UI_TOOLING)
            add(COMPOSE_RUNTIME)
            add(COMPOSE_ACTIVITY)
            add(COMPOSE_FOUNDATION)
            add(ANDROIDX_NAVIGATION_COMPOSE)
            add(COMPOSE_MATERIAL_ANDROID)
            add(COMPOSE_MATERIAL_ANDROID_ICON)
            add(COMPOSE_MATERIAL_ANDROID_ICON_EXT)
        }

    val composeImplLibsWearable =
        arrayListOf<String>().apply {
            add(COMPOSE_UI)
            add(COMPOSE_UI_TOOLING)
            add(COMPOSE_RUNTIME)
            add(COMPOSE_ACTIVITY)
            add(COMPOSE_MATERIAL_WEAR)
            add(COMPOSE_FOUNDATION_WEAR)
            add(ANDROIDX_NAVIGATION_COMPOSE)
            add(COMPOSE_LIFECYCLE_VIEWMODEL)
            add(COMPOSE_MATERIAL_ANDROID)
            add(COMPOSE_MATERIAL_ANDROID_ICON)
            add(COMPOSE_MATERIAL_ANDROID_ICON_EXT)
        }

    // UI Tools (Preview..)
    const val COMPOSE_UI_TOOLING = "androidx.compose.ui:ui-tooling:${Versions.Compose.UI}"
    const val COMPOSE_UI_MANIFEST = "androidx.compose.ui:ui-test-manifest:${Versions.Compose.UI}"
    const val ANDROIDX_COMTOM_VIEW =
        "androidx.customview:customview:${Versions.AndroidX.CUSTOM_VIEW}"
    const val ANDROIDX_CUSTOM_VIEW_CONTAINER =
        "androidx.customview:customview-poolingcontainer:${Versions.AndroidX.CUSTOM_VIEW_CONTAINER}"

    val uiDebugLibsMobile =
        arrayListOf<String>().apply {
            add(COMPOSE_UI_MANIFEST)
            add(ANDROIDX_COMTOM_VIEW)
            add(ANDROIDX_CUSTOM_VIEW_CONTAINER)
        }

    val uiDebugLibsWearable =
        arrayListOf<String>().apply {
            add(COMPOSE_UI_TOOLING)
            add(COMPOSE_UI_MANIFEST)
        }
}

fun DependencyHandler.kapt(list: List<String>) =
    list.forEach { dependency ->
        add("kapt", dependency)
    }

fun DependencyHandler.implementation(list: List<String>) =
    list.forEach { dependency ->
        add("implementation", dependency)
    }

fun DependencyHandler.debugImplementation(list: List<String>) =
    list.forEach { dependency ->
        add("debugImplementation", dependency)
    }

fun DependencyHandler.testRuntimeOnly(list: List<String>) =
    list.forEach { dependency ->
        add("testRuntimeOnly", dependency)
    }

fun DependencyHandler.testImplementation(list: List<String>) =
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }

fun DependencyHandler.androidTestImplementation(list: List<String>) =
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
