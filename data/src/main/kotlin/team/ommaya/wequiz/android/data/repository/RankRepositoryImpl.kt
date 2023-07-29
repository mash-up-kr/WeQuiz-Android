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
import team.ommaya.wequiz.android.data.model.rank.RankFormattedResponse
import team.ommaya.wequiz.android.domain.model.rank.Rank
import team.ommaya.wequiz.android.domain.repository.RankRepository
import javax.inject.Inject

class RankRepositoryImpl @Inject constructor(
    private val client: HttpClient,
) : RankRepository {
    override suspend fun getQuizRank(
        token: String,
        size: Int,
        cursorScore: Int?,
        cursorUserld: Int?,
    ): Rank {
        val response =
            client
                .get("ranking/my-quiz") {
                    header("x-wequiz-token", token)
                    parameter("size", size)
                    parameter("cursorScore", cursorScore)
                    parameter("cursorUserld", cursorUserld)
                }
                .body<RankFormattedResponse>()
        return requireNotNull(response.data).toDomain()
    }
}
