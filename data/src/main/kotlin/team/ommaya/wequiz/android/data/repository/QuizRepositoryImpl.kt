/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import team.ommaya.wequiz.android.data.client.TmpToken
import team.ommaya.wequiz.android.data.mapper.toQuestionDtoList
import team.ommaya.wequiz.android.data.model.quiz.QuizCreateRequest
import team.ommaya.wequiz.android.data.model.quiz.QuizCreateResponse
import team.ommaya.wequiz.android.domain.model.quiz.Question
import team.ommaya.wequiz.android.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val client: HttpClient,
) : QuizRepository {
    override suspend fun postQuiz(title: String, questions: List<Question>): Int {
        val response =
            client
                .post("quiz") {
                    header("x-wequiz-token: ", TmpToken)
                    setBody(
                        QuizCreateRequest(
                            title = title,
                            questions = questions.toQuestionDtoList(),
                        )
                    )
                }.body<QuizCreateResponse>()
        return response.quizId
    }
}
