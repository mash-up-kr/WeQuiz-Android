/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("TestFunctionName")

package team.ommaya.wequiz.android.design.resource.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@SuppressLint("IgnoreWithoutReason")
@RunWith(AndroidJUnit4::class)
@Ignore
class WeQuizGradientSnapshot {
    @Test
    fun Black() {
        captureRoboImage("src/test/snapshots/WeQuizGradient/black.png") {
            Box(
                Modifier
                    .size(300.dp)
                    .background(brush = WeQuizGradient.Black),
            )
        }
    }

    @Test
    fun Secondary() {
        captureRoboImage("src/test/snapshots/WeQuizGradient/secondary.png") {
            Box(
                Modifier
                    .size(300.dp)
                    .background(brush = WeQuizGradient.Secondary),
            )
        }
    }
}
