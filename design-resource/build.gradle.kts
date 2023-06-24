/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

plugins {
    android("library")
    kotlin("android")
}

android {
    namespace = "team.ommaya.wequiz.android.design.resource"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
    }

    sourceSets {
        getByName("main").java.srcDir("src/main/kotlin")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }
}
