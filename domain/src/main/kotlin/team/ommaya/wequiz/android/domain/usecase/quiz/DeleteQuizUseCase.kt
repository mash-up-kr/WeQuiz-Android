/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.usecase.quiz

import team.ommaya.wequiz.android.domain.repository.QuizRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteQuizUseCase @Inject constructor(
    private val repository: QuizRepository,
) {
    suspend operator fun invoke(token: String, quizId: Int) =
        runCatching {
            repository.deleteQuiz(token = token, quizId = quizId)
        }
}
