/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

plugins {
    wequiz("android-library")
    alias(libs.plugins.andrdoix.navigation.safeargs)
}

android {
    namespace = "team.ommaya.wequiz.quiz.create"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementations(libs.bundles.androidx.xml)
    implementations(libs.glide)
    implementations(libs.kotlinx.coroutine)
}
