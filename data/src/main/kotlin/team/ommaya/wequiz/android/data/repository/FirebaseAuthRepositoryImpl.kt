/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.repository

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import team.ommaya.wequiz.android.domain.AuthCallbacksListener
import team.ommaya.wequiz.android.domain.repository.FirebaseAuthRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : FirebaseAuthRepository {
    private lateinit var verificationID: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var authCallbacksListener: AuthCallbacksListener
    private lateinit var optionsBuilder: PhoneAuthOptions.Builder
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // 문자가 오면 자동으로 인증하는 코드
//            val verifyCode = credential.smsCode
//
//            if (!verifyCode.isNullOrEmpty()) {
//                authCallbacksListener.onVerificationCompleted(verifyCode)
//            }
//
//            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(firebaseException: FirebaseException) {
            if (firebaseException is FirebaseAuthInvalidCredentialsException) {
                authCallbacksListener.onVerificationFailed("전화번호 오류")
            } else if (firebaseException is FirebaseTooManyRequestsException) {
                authCallbacksListener.onVerificationFailed("SMS 할당량 초과 오류")
            } else if (firebaseException is FirebaseNetworkException) {
                authCallbacksListener.onVerificationFailed("네트워크 오류")
            } else if (firebaseException is FirebaseAuthMissingActivityForRecaptchaException) {
                authCallbacksListener.onVerificationFailed("null Activity에서 reCAPTCHA 시도 오류")
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            super.onCodeSent(verificationId, token)
            // 전화번호로 인증 코드가 SMS를 통해 전송된 후에 호출
            // 사용자가 인증 코드를 입력하면 인증 코드와 이 메서드에 전달된 인증 ID를 사용하여 PhoneAuthCredential 객체를 만들고 로그인 처리
            verificationID = verificationId
            resendToken = token

            authCallbacksListener.onCodeSent(verificationId, resendToken)
        }
    }

    override fun setFirebaseAuthCallbacksListener(listener: AuthCallbacksListener) {
        this.authCallbacksListener = listener
    }

    override suspend fun startPhoneNumberVerification(phoneNumber: String, activity: Activity) {
        optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+82${phoneNumber.replace("-", "")}")
            .setTimeout(120_000L, TimeUnit.MILLISECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    override suspend fun resendVerifyCode(activity: Activity) {
        optionsBuilder.setForceResendingToken(resendToken)

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    override fun verifyPhoneNumberWithCode(verifyCode: String) {
        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationID, verifyCode))
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) { // 인증 성공
                val user = task.result?.user
                if (user != null) {
                    // uid 저장 및 회원가입/로그인 서버 api 호출
                    authCallbacksListener.onVerificationSuccess(user.uid)
                }
            } else { // 인증 실패
                when (task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> { // 인증 번호 불일치
                        // 인증번호가 올바르지 않아요 다이얼로그 출력
                        authCallbacksListener.onVerifyCodeInvalid()
                    }
                }
            }
        }
    }
}
