/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.model.quiz

data class QuizResult(
    val score: Int = 0,
    val creatorId: Int = 0,
    val creatorName: String = "",
    val resolverId: Int = 0,
    val resolverName: String = "",
)
