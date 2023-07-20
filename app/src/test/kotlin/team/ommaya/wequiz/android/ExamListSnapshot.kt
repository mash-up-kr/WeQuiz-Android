/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("TestFunctionName")

package team.ommaya.wequiz.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.dummy.DummyQuizs
import team.ommaya.wequiz.android.home.quizlist.QuizList
import team.ommaya.wequiz.android.rule.SnapshotPathGeneratorRule

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7Pro)
@RunWith(AndroidJUnit4::class)
class QuizListSnapshot {
    @get:Rule
    val snapshotPath = SnapshotPathGeneratorRule("QuizList")

    @Test
    fun DeleteModeOn() {
        captureRoboImage(snapshotPath()) {
            QuizList(
                modifier = Modifier
                    .background(color = WeQuizColor.G9.value)
                    .padding(20.dp)
                    .fillMaxSize(),
                quizs = DummyQuizs,
                deleteModeEnable = true,
            )
        }
    }

    @Test
    fun DeleteModeOff() {
        captureRoboImage(snapshotPath()) {
            QuizList(
                modifier = Modifier
                    .background(color = WeQuizColor.G9.value)
                    .padding(20.dp)
                    .fillMaxSize(),
                quizs = DummyQuizs,
                deleteModeEnable = false,
            )
        }
    }
}
