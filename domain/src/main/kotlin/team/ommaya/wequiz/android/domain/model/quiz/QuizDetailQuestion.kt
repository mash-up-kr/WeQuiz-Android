/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.model.quiz

data class QuizDetailQuestion(
    val score: Int,
    val answerCounts: Int,
    val options: List<QuizDetailOption>,
    val id: Int,
    val title: String,
)
