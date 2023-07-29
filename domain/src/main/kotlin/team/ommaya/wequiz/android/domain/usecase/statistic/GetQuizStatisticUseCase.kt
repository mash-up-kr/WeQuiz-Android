/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.usecase.statistic

import team.ommaya.wequiz.android.domain.repository.StatisticRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetQuizStatisticUseCase @Inject constructor(
    private val repository: StatisticRepository,
) {
    suspend operator fun invoke(
        token: String,
        quizId: Int,
    ) =
        runCatching {
            repository.getQuizStatistic(token = token, quizId = quizId)
        }
}
