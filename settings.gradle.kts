/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("UnstableApiUsage")

rootProject.name = "WeQuiz-Android"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    includeBuild("build-logic")
}

buildCache {
    local {
        removeUnusedEntriesAfterDays = 7
    }
}

include(
    ":app",
    ":join",
    ":home:main",
    ":home:profile-edit",
    ":home:friend-rank",
    ":home:quiz-list",
    ":home:quiz-detail",
    ":quiz:create",
    ":design-resource",
    ":util:view"
)
