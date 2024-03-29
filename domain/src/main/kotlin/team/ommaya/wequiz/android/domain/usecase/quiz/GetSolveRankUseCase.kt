/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.usecase.quiz

import team.ommaya.wequiz.android.domain.repository.RankRepository
import team.ommaya.wequiz.android.domain.runSuspendCatching
import javax.inject.Inject

class GetSolveRankUseCase @Inject constructor(
    private val rankRepository: RankRepository,
) {
    suspend operator fun invoke(quizId: Int) =
        runSuspendCatching {
            rankRepository.getSolveRank(quizId, 30, null)
        }
}
