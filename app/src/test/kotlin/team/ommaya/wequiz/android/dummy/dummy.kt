/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.dummy

import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.ommaya.wequiz.android.home.QuizNameAndIsWritingPair
import team.ommaya.wequiz.android.home.NicknameUuidScoreTriple

val DummyFriendsRanking =
    List(30) { index ->
        @Suppress("NAME_SHADOWING")
        val index = index + 1
        NicknameUuidScoreTriple(
            /*nickname = */ "${index}번_친구${if (index >= 10) "_친" else ""}".take(8),
            /*uuid = */ index * 1_000,
            /*score = */ index * 100,
        )
    }.toImmutableList()

val DummyQuizs =
    List(30) { index ->
        @Suppress("NAME_SHADOWING")
        val index = index + 1
        val boolean = index % 2 == 0
        QuizNameAndIsWritingPair(
            /*QuizName = */ "${index}번${"_시험지".repeat(10)}".take(if (!boolean) 10 else 38),
            /*isWip = */ boolean,
        )
    }.toPersistentList()
