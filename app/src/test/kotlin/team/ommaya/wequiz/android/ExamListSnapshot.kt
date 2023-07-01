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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.RoborazziRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.dummy.DummyExams
import team.ommaya.wequiz.android.home.ExamList
import team.ommaya.wequiz.android.rule.BaseSnapshotPath
import java.io.File

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7Pro)
@RunWith(AndroidJUnit4::class)
class ExamListSnapshot {
    /*@get:Rule
    val snapshotPath = SnapshotPathGeneratorRule("ExamList")*/

    @get:Rule
    val compose = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazzi = RoborazziRule(
        composeRule = compose,
        captureRoot = compose.onRoot(),
        options = RoborazziRule.Options(
            captureType = RoborazziRule.CaptureType.AllImage,
            outputFileProvider = { description, _, fileExtension ->
                File("$BaseSnapshotPath/ExamList/${description.methodName}.$fileExtension")
                    .also { it.parentFile?.mkdirs() }
            },
        ),
    )

    @Test
    fun DeleteModeToggle() {
        /*captureRoboImage(snapshotPath()) {*/
        compose.setContent {
            var deleteMode by remember { mutableStateOf(false) }
            ExamList(
                modifier = Modifier
                    .background(color = WeQuizColor.G9.value)
                    .padding(20.dp)
                    .fillMaxSize(),
                exams = DummyExams,
                deleteModeEnable = deleteMode,
            )
            Box(
                Modifier
                    .testTag("deleteModeToggle")
                    .size(50.dp)
                    .clickable { deleteMode = true },
            )
        }
        compose.onNodeWithTag("deleteModeToggle").performClick()
    }

    /* @Test
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
     }*/
}
