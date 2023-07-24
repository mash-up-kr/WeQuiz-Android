/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.repository

import team.ommaya.wequiz.android.domain.model.quiz.Question

interface QuizRepository {
    suspend fun postQuiz(title: String, questions: List<Question>): Int
}
