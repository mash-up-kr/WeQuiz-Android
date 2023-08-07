/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.usecase.quiz

import team.ommaya.wequiz.android.domain.repository.QuizRepository
import team.ommaya.wequiz.android.domain.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetQuizListUseCase @Inject constructor(
    private val repository: QuizRepository,
) {
    suspend operator fun invoke(
        token: String,
        size: Int = 15,
        cursor: Int? = null,
    ) =
        runSuspendCatching {
            repository.getQuizList(token, size, cursor)
        }
}
