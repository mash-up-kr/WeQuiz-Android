/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("TestFunctionName", "PrivatePropertyName")

package team.ommaya.wequiz.android

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import team.ommaya.wequiz.android.dummy.DummyExams
import team.ommaya.wequiz.android.dummy.DummyFriendsRanking
import team.ommaya.wequiz.android.home.HomeMainScreen
import team.ommaya.wequiz.android.rule.SnapshotPathGeneratorRule

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7Pro)
@RunWith(AndroidJUnit4::class)
class HomeMainScreenSnapshot {
    @get:Rule
    val snapshotPath = SnapshotPathGeneratorRule("HomeMainScreen")

    private val DummyProfileImage =
        Bitmap
            .createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            .applyCanvas {
                drawColor(Color.parseColor("#71F7FF"))
            }

    @Test
    fun Empty() {
        captureRoboImage(snapshotPath()) {
            HomeMainScreen(
                nickname = "닉네임",
                profileMessage = "",
                profileImageSrc = DummyProfileImage,
                friendsRanking = persistentListOf(),
                exams = persistentListOf(),
            )
        }
    }

    @Test
    fun OnlyExam() {
        captureRoboImage(snapshotPath()) {
            HomeMainScreen(
                nickname = "닉네임",
                profileMessage = "프로필 메시지",
                profileImageSrc = DummyProfileImage,
                friendsRanking = persistentListOf(),
                exams = DummyExams,
            )
        }
    }

    @Test
    fun FriendsRankingAndExam() {
        captureRoboImage(snapshotPath()) {
            HomeMainScreen(
                nickname = "닉네임",
                profileMessage = "프로필 메시지\n프로필 메시지",
                profileImageSrc = DummyProfileImage,
                friendsRanking = DummyFriendsRanking,
                exams = DummyExams,
            )
        }
    }
}

private inline fun Bitmap.applyCanvas(block: Canvas.() -> Unit): Bitmap {
    val canvas = Canvas(this)
    canvas.block()
    return this
}
