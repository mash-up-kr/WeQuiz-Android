/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.repository

import android.app.Activity
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import team.ommaya.wequiz.android.domain.AuthCallbacksListener

interface FirebaseRepository {
    fun setFirebaseAuthCallbacksListener(listener: AuthCallbacksListener)

    suspend fun startPhoneNumberVerification(phoneNumber: String, activity: Activity)

    suspend fun resendVerifyCode(activity: Activity)

    fun verifyPhoneNumberWithCode(verifyCode: String)

    fun setFirebasePlayIntegrity()

    fun makeInvitationLink(quizId: Int): Flow<Uri>
}
