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
import kotlinx.collections.immutable.toImmutableList
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import team.ommaya.wequiz.android.home.main.ExamNameAndIsWritingPair
import team.ommaya.wequiz.android.home.main.HomeMainScreen
import team.ommaya.wequiz.android.home.main.NicknameUuidScoreTriple

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7Pro)
@RunWith(AndroidJUnit4::class)
class HomeMainScreenSnapshot {
    private val DummyProfileImage =
        Bitmap
            .createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            .applyCanvas {
                drawColor(Color.parseColor("#71F7FF"))
            }
    private val DummyFriendsRanking =
        List(10) { index ->
            @Suppress("NAME_SHADOWING")
            val index = index + 1
            NicknameUuidScoreTriple("${index}번_친구_친구", index * 1_000, index * 100)
        }.toImmutableList()
    private val DummyExams =
        List(10) { index ->
            @Suppress("NAME_SHADOWING")
            val index = index + 1
            val boolean = index % 2 == 0
            ExamNameAndIsWritingPair(
                "${index}번${"_시험지".repeat(10)}".take(if (!boolean) 10 else 38),
                boolean,
            )
        }.toImmutableList()

    @Test
    fun Empty() {
        captureRoboImage("src/test/snapshots/HomeMainScreen/Empty.png") {
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
        captureRoboImage("src/test/snapshots/HomeMainScreen/OnlyExam.png") {
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
        captureRoboImage("src/test/snapshots/HomeMainScreen/FriendsRankingAndExam.png") {
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
