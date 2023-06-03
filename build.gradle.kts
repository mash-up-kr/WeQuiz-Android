/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.kotlin.detekt)
    alias(libs.plugins.kotlin.ktlint)
    alias(libs.plugins.gradle.dependency.handler.extensions)
    id("com.android.library") version "8.0.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.kotlin.gradle)
        classpath(libs.gradle.android)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply {
        plugin(rootProject.libs.plugins.kotlin.detekt.get().pluginId)
        plugin(rootProject.libs.plugins.kotlin.ktlint.get().pluginId)
        plugin(rootProject.libs.plugins.gradle.dependency.handler.extensions.get().pluginId)
    }

    afterEvaluate {
        extensions.configure<DetektExtension> {
            parallel = true
            buildUponDefaultConfig = true
            toolVersion = libs.versions.kotlin.detekt.get()
            config.setFrom(files("$rootDir/detekt-config.yml"))
        }

        extensions.configure<KtlintExtension> {
            version.set(rootProject.libs.versions.kotlin.ktlint.source.get())
            android.set(true)
            verbose.set(true)
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-opt-in=kotlin.OptIn",
                    "-opt-in=kotlin.RequiresOptIn",
                )
            }
        }
    }
}

tasks.register(name = "cleanAll", type = Delete::class) {
    allprojects.map(Project::getBuildDir).forEach(::delete)
}
