/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import team.ommaya.wequiz.android.data.mapper.toDomain
import team.ommaya.wequiz.android.data.model.statistic.QuizStatisticResponse
import team.ommaya.wequiz.android.domain.model.statistic.QuizStatistic
import team.ommaya.wequiz.android.domain.repository.StatisticRepository
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor(
    private val client: HttpClient,
) : StatisticRepository {
    override suspend fun getQuizStatistic(token: String, quizId: Int): QuizStatistic {
        val response =
            client
                .get("statistic/quiz/$quizId") {
                    header("x-wequiz-token", token)
                }
                .body<QuizStatisticResponse>()
        return response.toDomain()
    }
}
