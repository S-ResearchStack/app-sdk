buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:${Versions.JUnit.PLUGIN}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.Hilt.DAGGER}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.DETEKT}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${Versions.DOKKA}")
        classpath("com.google.gms:google-services:${Versions.Google.GMS}")
        classpath("com.google.protobuf:protobuf-gradle-plugin:${Versions.GRPC.PROTOBUF}")
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version Versions.KTLINT apply false
    kotlin("kapt") version Versions.KOTLIN apply false
    id("com.google.protobuf") version Versions.GRPC.PROTOBUF apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN apply false
    id("nl.neotech.plugin.rootcoverage") version Versions.ROOT_COVERAGE
}

subprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory.asFile)
}

val testWithLint by tasks.registering {
    group = "verification"
    description = "run unit tests with lint check"

    dependsOn(
        getTasksByName("test", true),
        getTasksByName("ktlintCheck", true),
    )
}

jacoco {
    toolVersion = Versions.JACOCO
}

rootCoverage {
    generateCsv = false
    generateHtml = true
    generateXml = true

    excludes =
        listOf(
            "**/config/**",
            "**/Dagger*",
            "**/hilt_aggregated_deps/**",
            "**/Hilt*",
            "**/*_Hilt*",
            "**/*ComponentTreeDeps",
            "**/codegen/**",
            "**/presentation/**",
            "**/generated/**",
            "**/*_Impl*",
            "**/ResearchApplication*",
            "**/*MainActivity*",
            "**/com/msc/**",
            "**/com/sec/healthresearch/backend/grpc/**",
            "**/ISACallbackStubAdapter.class",
            "**/ResearchAppDatabase*",
            "**/SHealthDataSource*",
        )

    executeAndroidTests = false
    executeUnitTests = true
    includeAndroidTestResults = false
    includeUnitTestResults = true
    includeNoLocationClasses = false
}
