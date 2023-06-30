/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

plugins {
    android("application")
    kotlin("android")
    alias(libs.plugins.test.roborazzi)
}

android {
    namespace = "team.ommaya.wequiz.android"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        @Suppress("OldTargetApi")
        targetSdk = 33
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
        libs.androidx.constraintlayout,
        libs.androidx.fragment,
        libs.androidx.navigation.fragment,
        libs.androidx.navigtaion.ui.ktx,
        libs.androidx.lifecycle.runtime,
        libs.androidx.lifecycle.viewmodel,
        libs.compose.runtime,
        libs.compose.ui,
        libs.compose.uiutil,
        libs.compose.foundation,
        libs.kotlinx.collections.immutable,
        libs.coil.compose,
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
    testRuntimeOnly(libs.test.junit.engine)
}
