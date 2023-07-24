/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain

import com.google.firebase.auth.PhoneAuthProvider

interface AuthCallbacksListener {
    fun onVerificationSuccess(uid: String)

    fun onVerificationFailed(message: String)

    fun onVerifyCodeInvalid()

    fun onVerificationCompleted(verifyCode: String)

    fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?)
}
