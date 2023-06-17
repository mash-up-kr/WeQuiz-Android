/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

plugins {
    wequiz("android-library")
    wequiz("android-compose")
    wequiz("android-library-roborazzi")
}

android {
    namespace = "team.ommaya.wequiz.android.home.main"
}

dependencies {
    implementations(
        libs.compose.runtime,
        libs.compose.ui,
        libs.compose.uiutil,
        libs.compose.foundation,
        libs.kotlinx.collections.immutable,
        libs.coil.compose,
        // libs.coil.gif, (gif support needs?)
        projects.designResourceCompose,
    )
}
