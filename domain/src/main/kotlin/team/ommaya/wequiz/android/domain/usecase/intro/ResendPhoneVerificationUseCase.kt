/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.usecase.intro

import android.app.Activity
import team.ommaya.wequiz.android.domain.repository.FirebaseRepository
import javax.inject.Inject

class ResendPhoneVerificationUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
) {
    suspend operator fun invoke(activity: Activity) = firebaseRepository.resendVerifyCode(activity)
}
