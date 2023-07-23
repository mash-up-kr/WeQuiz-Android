/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.quiz

import team.ommaya.wequiz.android.domain.model.quiz.Question

data class QuizCreateRequest(
    val title: String = "",
    val questions: List<Question> = emptyList(),
)
