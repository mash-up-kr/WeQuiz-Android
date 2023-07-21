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
import io.ktor.client.request.parameter
import team.ommaya.wequiz.android.data.mapper.toDomain
import team.ommaya.wequiz.android.data.model.rank.RankResponse
import team.ommaya.wequiz.android.domain.model.rank.Rank
import team.ommaya.wequiz.android.domain.repository.RankRepository
import javax.inject.Inject

class RankRepositoryImpl @Inject constructor(
    private val client: HttpClient,
) : RankRepository {
    override suspend fun getMyQuizRank(
        token: String,
        size: Int,
        quizAnswerCursorId: Int?,
    ): Rank {
        val response = client.get("ranking/my-quiz") {
            parameter("size", size)
            parameter("quizAnswerCursorId", quizAnswerCursorId)
            header("x-wequiz-token", token)
        }.body<RankResponse>()
        return response.toDomain()
    }
}
