/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import team.ommaya.wequiz.android.data.client.TmpToken
import team.ommaya.wequiz.android.data.mapper.toDomain
import team.ommaya.wequiz.android.data.mapper.toQuestionDtoList
import team.ommaya.wequiz.android.data.mapper.toQuizResult
import team.ommaya.wequiz.android.data.model.quiz.QuizCreateRequest
import team.ommaya.wequiz.android.data.model.quiz.QuizCreateResponse
import team.ommaya.wequiz.android.data.model.quiz.QuizDetailFormattedResponse
import team.ommaya.wequiz.android.data.model.quiz.QuizListFormattedResponse
import team.ommaya.wequiz.android.data.model.quiz.SubmitAnswerRequest
import team.ommaya.wequiz.android.data.model.quiz.SubmitAnswerResponse
import team.ommaya.wequiz.android.domain.model.quiz.Answer
import team.ommaya.wequiz.android.domain.model.quiz.Creator
import team.ommaya.wequiz.android.domain.model.quiz.Question
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetail
import team.ommaya.wequiz.android.domain.model.quiz.QuizList
import team.ommaya.wequiz.android.domain.model.quiz.QuizResult
import team.ommaya.wequiz.android.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val client: HttpClient,
) : QuizRepository {
    override suspend fun getQuizList(
        token: String,
        size: Int,
        cursor: Int?,
    ): QuizList {
        val response =
            client
                .get("quiz") {
                    header("x-wequiz-token", token)
                    parameter("size", size)
                    parameter("cursor", cursor)
                }
                .body<QuizListFormattedResponse>()
        return requireNotNull(response.data).toDomain()
    }

    override suspend fun postQuiz(title: String, questions: List<Question>): Int {
        val response =
            client
                .post("quiz") {
                    header("x-wequiz-token", TmpToken)
                    setBody(
                        QuizCreateRequest(
                            title = title,
                            questions = questions.toQuestionDtoList(),
                        ),
                    )
                }
                .body<QuizCreateResponse>()
        if (response.code == "SUCCESS") {
            return response.data.quizId
        } else {
            throw Exception("response.message")
        }
    }

    override suspend fun getQuizDetail(quizId: Int): QuizDetail {
        val response =
            client
                .get("quiz/$quizId")
                .body<QuizDetailFormattedResponse>()
        if (response.code == "SUCCESS") {
            return response.data?.toDomain() ?: QuizDetail(emptyList(), 0, Creator(), "")
        } else {
            throw Exception("code: ${response.code} message: ${response.message}")
        }
    }

    override suspend fun deleteQuiz(token: String, quizId: Int) {
        client.delete("quiz/$quizId") { header("x-wequiz-token", token) }
    }

    override suspend fun submitQuizAnswer(quizId: Int, answerList: List<Answer>): QuizResult {
        val response =
            client
                .post("quiz/$quizId/answers") {
                    header("x-wequiz-token", TmpToken)
                    setBody(
                        SubmitAnswerRequest(
                            answerList
                        )
                    )
                }
                .body<SubmitAnswerResponse>()
        if (response.code == "SUCCESS") {
            return response.data.toQuizResult()
        } else {
            throw Exception(response.message)
        }
    }
}
