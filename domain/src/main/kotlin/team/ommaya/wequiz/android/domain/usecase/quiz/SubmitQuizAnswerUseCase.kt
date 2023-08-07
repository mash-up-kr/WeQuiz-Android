/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.usecase.quiz

import team.ommaya.wequiz.android.domain.model.quiz.Answer
import team.ommaya.wequiz.android.domain.repository.QuizRepository
import team.ommaya.wequiz.android.domain.runSuspendCatching
import javax.inject.Inject

class SubmitQuizAnswerUseCase @Inject constructor(
    private val quizRepository: QuizRepository,
) {
    suspend operator fun invoke(token: String, quizId: Int, answerList: List<Answer>) =
        runSuspendCatching {
            quizRepository.submitQuizAnswer(token, quizId, answerList)
        }
}
