/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.quiz

import kotlinx.serialization.Serializable

@Serializable
data class SubmitAnswerResponse(
    val code: String = "",
    val data: SubmitResult = SubmitResult(),
    val message: String = "",
)

@Serializable
data class SubmitResult(
    val quizCreator: Creator = Creator(),
    val quizResolver: Resolver = Resolver(),
    val totalScore: Int = 0,
)

@Serializable
data class Creator(
    val id: Int = 0,
    val name: String = "",
)

@Serializable
data class Resolver(
    val id: Int = 0,
    val name: String = "",
)
