buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
        classpath("com.android.tools.build:gradle:${Versions.ANDROID_BUILD}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:${Versions.JUnit.PLUGIN}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.Hilt.DAGGER}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.DETEKT}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:${Versions.DOKKA}")
        classpath("com.google.gms:google-services:${Versions.Google.GMS}")
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version Versions.KTLINT apply false
    kotlin("kapt") version Versions.KOTLIN apply false
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
    delete(rootProject.buildDir)
}

val testWithLint by tasks.registering {
    group = "verification"
    description = "run unit tests with lint check"

    dependsOn(
        getTasksByName("test", true),
        getTasksByName("ktlintCheck", true)
    )
}
