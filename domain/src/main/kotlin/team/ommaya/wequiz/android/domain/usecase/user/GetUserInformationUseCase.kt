/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.usecase.user

import team.ommaya.wequiz.android.domain.repository.UserRepository
import team.ommaya.wequiz.android.domain.runSuspendCatching
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserInformationUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(token: String) =
        runSuspendCatching {
            repository.getInformation(token)
        }
}
