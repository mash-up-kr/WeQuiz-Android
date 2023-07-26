/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    android("application")
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.test.roborazzi)
    alias(libs.plugins.google.services)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "team.ommaya.wequiz.android"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    sourceSets {
        getByName("main").java.srcDir("src/main/kotlin")
        getByName("test").java.srcDir("src/test/kotlin")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all { test ->
                test.useJUnitPlatform()
                test.systemProperty("robolectric.graphicsMode", "NATIVE")

                if (!test.name.contains("debug", ignoreCase = true)) {
                    test.enabled = false
                }
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    // https://stackoverflow.com/a/36178581/14299073
    outputs.upToDateWhen { false }
    testLogging {
        events = setOf(
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED,
            TestLogEvent.FAILED,
        )
    }
    afterSuite(
        KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
            if (desc.parent == null) { // will match the outermost suite
                val output = "Results: ${result.resultType} " +
                        "(${result.testCount} tests, " +
                        "${result.successfulTestCount} passed, " +
                        "${result.failedTestCount} failed, " +
                        "${result.skippedTestCount} skipped)"
                println(output)
            }
        }),
    )
}

tasks.matching { task ->
    task.name.contains("compareRoborazzi", ignoreCase = true) ||
            task.name.contains("verifyRoborazzi", ignoreCase = true) ||
            task.name.contains("verifyAndRecordRoborazzi", ignoreCase = true)
}.configureEach {
    enabled = false
}

dependencies {
    implementations(
        libs.material,
        libs.coil.compose,
        libs.androidx.constraintlayout,
        libs.androidx.fragment,
        libs.androidx.navigation.fragment,
        libs.androidx.navigtaion.ui.ktx,
        libs.androidx.lifecycle.runtime,
        libs.androidx.lifecycle.viewmodel,
        libs.kotlinx.collections.immutable,
        libs.bundles.firebase,
        libs.compose.runtime,
        libs.compose.ui,
        libs.compose.uiutil,
        libs.compose.foundation,
        libs.compose.activity,
        libs.android.hilt.runtime,
        projects.data,
        projects.domain,
        projects.designResource,
        projects.designResourceCompose,
    )
    testImplementations(
        libs.compose.activity, // needed for roborazzi that used ActivityScenario internally
        libs.compose.foundation,
        libs.test.junit.core,
        libs.test.androidx.junit.ktx,
        libs.test.robolectric,
        libs.bundles.test.roborazzi,
    )
    kapt(libs.android.hilt.compile)
    testRuntimeOnly(libs.test.junit.engine)
}
