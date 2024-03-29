/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.usecase.quiz

import team.ommaya.wequiz.android.domain.repository.FirebaseRepository
import javax.inject.Inject

class MakeInvitationLinkUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
) {
    operator fun invoke(quizId: Int) = firebaseRepository.makeInvitationLink(quizId)
}
