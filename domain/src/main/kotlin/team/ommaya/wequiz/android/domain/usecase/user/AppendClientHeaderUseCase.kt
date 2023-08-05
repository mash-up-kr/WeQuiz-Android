/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.usecase.user

import team.ommaya.wequiz.android.domain.repository.UserRepository
import javax.inject.Inject

class AppendClientHeaderUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(token: String) =
        runCatching {
            userRepository.appendClientHeader(token)
        }
}
