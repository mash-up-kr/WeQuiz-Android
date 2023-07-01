/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("TestFunctionName")

package team.ommaya.wequiz.android

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.dummy.DummyExams
import team.ommaya.wequiz.android.home.ExamList
import team.ommaya.wequiz.android.rule.SnapshotPathGeneratorRule

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7Pro)
@RunWith(AndroidJUnit4::class)
class ExamListSnapshot {
    @get:Rule
    val snapshotPath = SnapshotPathGeneratorRule("ExamList")

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = RoborazziRule(
        composeRule = composeTestRule,
        captureRoot = composeTestRule.onRoot(),
        options = RoborazziRule.Options(
            RoborazziRule.CaptureType.Gif
        )
    )

    /*@Before
    fun setUp() {
        AnimationSpecTestingSupports.isTestMode = true
    }*/

    @Test
    fun DeleteModeOn() {
        captureRoboImage(
            snapshotPath(isGif = true),
            roborazziOptions = RoborazziOptions(captureType = RoborazziRule.CaptureType.Gif)
        ) {
            ExamList(
                modifier = Modifier
                    .background(color = WeQuizColor.G9.value)
                    .padding(20.dp)
                    .fillMaxSize(),
                exams = DummyExams,
                deleteModeEnable = true,
            )
        }
    }

    @Test
    fun DeleteModeOff() {
        captureRoboImage(snapshotPath()) {
            ExamList(
                modifier = Modifier
                    .background(color = WeQuizColor.G9.value)
                    .padding(20.dp)
                    .fillMaxSize(),
                exams = DummyExams,
                deleteModeEnable = false,
            )
        }
    }

    /*@After
    fun tearDown() {
        AnimationSpecTestingSupports.isTestMode = false
    }*/
}
