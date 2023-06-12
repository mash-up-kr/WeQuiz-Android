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
    namespace = "team.ommaya.wequiz.android.join"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.bundles.androidx.xml)
    implementation(libs.glide)
    implementation(libs.kotlinx.coroutine)
}
