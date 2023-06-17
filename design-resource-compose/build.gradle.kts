/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("INLINE_FROM_HIGHER_PLATFORM", "UnstableApiUsage")

import org.gradle.kotlin.dsl.withType
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.KotlinClosure2

plugins {
    wequiz("android-library")
    wequiz("android-compose")
    wequiz("kotlin-explicit-api")
    alias(libs.plugins.test.roborazzi)
}

android {
    namespace = "team.ommaya.wequiz.android.design.resource.compose"
    resourcePrefix = "drc_"

    sourceSets {
        getByName("test").java.srcDir("src/test/kotlin")
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

dependencies {
    implementations(
        libs.compose.runtime,
        libs.compose.ui,
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
