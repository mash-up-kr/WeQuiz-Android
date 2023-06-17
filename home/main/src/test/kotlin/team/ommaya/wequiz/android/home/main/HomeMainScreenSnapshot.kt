/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("TestFunctionName")

package team.ommaya.wequiz.android.home.main

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeMainScreenSnapshot {
    @Test
    fun HomeMainScreenSnapshot() {
        captureRoboImage("src/test/snapshots/WeQuizGradient/HomeMainScreen-empty.png") {
            HomeMainScreen(
                nickname = "닉네임",
                profileMessage = "프로필 메시지\n프로필 메시지",
                profileImageSrc = Bitmap
                    .createBitmap(
                        100,
                        100,
                        Bitmap.Config.ARGB_8888,
                    )
                    .applyCanvas {
                        drawColor(android.graphics.Color.YELLOW)
                    },
                friendsRanking = persistentListOf(),
                examPagers = persistentListOf(),
            )
        }
    }
}

private inline fun Bitmap.applyCanvas(block: Canvas.() -> Unit): Bitmap {
    val canvas = Canvas(this)
    canvas.block()
    return this
}
