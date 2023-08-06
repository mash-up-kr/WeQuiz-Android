/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.repository

import team.ommaya.wequiz.android.domain.model.quiz.Answer
import team.ommaya.wequiz.android.domain.model.quiz.Question
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetail
import team.ommaya.wequiz.android.domain.model.quiz.QuizList
import team.ommaya.wequiz.android.domain.model.quiz.QuizResult

interface QuizRepository {
    suspend fun getQuizList(token: String, size: Int, cursor: Int?): QuizList
    suspend fun postQuiz(title: String, questions: List<Question>): Int
    suspend fun getQuizDetail(quizId: Int): QuizDetail
    suspend fun deleteQuiz(token: String, quizId: Int)
    suspend fun submitQuizAnswer(token: String, quizId: Int, answerList: List<Answer>): QuizResult
}
