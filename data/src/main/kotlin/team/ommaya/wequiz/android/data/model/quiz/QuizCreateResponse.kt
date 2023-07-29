/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.quiz

data class QuizCreateResponse(
    val code: String = "",
    val data: QuizInfo = QuizInfo(),
    val message: String = "",
)

data class QuizInfo(
    val quizId: Int = 0,
)
