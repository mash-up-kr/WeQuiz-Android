/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.repository

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import team.ommaya.wequiz.android.domain.AuthCallbacksListener
import team.ommaya.wequiz.android.domain.repository.FirebaseRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dynamicLinks: FirebaseDynamicLinks,
    @ApplicationContext private val context: Context,
    private val firebase: Firebase,
) : FirebaseRepository {
    private lateinit var verificationID: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var authCallbacksListener: AuthCallbacksListener
    private lateinit var optionsBuilder: PhoneAuthOptions.Builder
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // 문자가 오면 자동으로 인증하는 코드
//            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(firebaseException: FirebaseException) {
            authCallbacksListener.onVerificationFailed(firebaseException)
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

    override fun setFirebasePlayIntegrity() {
        firebase.initialize(context)
        firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
    }

    override fun makeInvitationLink(quizId: Int) = callbackFlow {
        val invitationLink =
            "https://wequiz.page.link/?link=https://wequiz.page.link/solve?quizId=$quizId&apn=team.ommaya.wequiz.android&isi=6453208230&ibi=wequiz.ios.WeQuiz"
        dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.UNGUESSABLE) {
            link = Uri.parse(invitationLink)
            domainUriPrefix = "https://wequiz.page.link"
            socialMetaTagParameters {
                title = "진정한 친구들만 통과할 수 있는 찐친고사"
                description = "너 나 알아? WeQuiz 우정테스트"
                imageUrl =
                    Uri.parse("https://firebasestorage.googleapis.com/v0/b/wequiz-3910f.appspot.com/o/metaTagImage.png?alt=media&token=22389993-44e2-4ffa-adce-0a8f369d0173")
            }
        }.addOnSuccessListener { shortDynamicLink ->
            val shortLinkUri = shortDynamicLink.shortLink
            trySend(shortLinkUri ?: Uri.EMPTY)
        }.addOnFailureListener {
            trySend(Uri.EMPTY)
        }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) { // 인증 성공
                val firebaseUser = task.result?.user
                firebaseUser?.let { user ->
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
