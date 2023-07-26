/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.domain.AuthCallbacksListener
import team.ommaya.wequiz.android.domain.usecase.intro.ResendPhoneVerificationUseCase
import team.ommaya.wequiz.android.domain.usecase.intro.SetAuthCallbacksUseCase
import team.ommaya.wequiz.android.domain.usecase.intro.StartPhoneVerificationUseCase
import team.ommaya.wequiz.android.domain.usecase.intro.VerifyCodeUseCase
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val setAuthCallbacksUseCase: SetAuthCallbacksUseCase,
    private val startPhoneVerificationUseCase: StartPhoneVerificationUseCase,
    private val resendPhoneVerificationUseCase: ResendPhoneVerificationUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase,
) : ViewModel(), AuthCallbacksListener {
    private val _mode: MutableStateFlow<IntroMode> = MutableStateFlow(IntroMode.LOGIN)
    val mode = _mode.asStateFlow()

    private val _isVerifyTimeOut: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isVerifyTimeOut = _isVerifyTimeOut.asStateFlow()

    private val _verifyTime: MutableStateFlow<String> = MutableStateFlow(INITIAL_VERIFY_TIME)
    val verifyTime = _verifyTime.asStateFlow()

    private val _verifyCodeEventFlow: MutableSharedFlow<VerifyCodeUiEvent> = MutableSharedFlow()
    val verifyCodeEventFlow = _verifyCodeEventFlow.asSharedFlow()

    private val _nickname: MutableStateFlow<String> = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private val _isUserRegistered: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUserRegistered = _isUserRegistered.asStateFlow()

    init {
        setAuthCallbacksUseCase(this)
    }

    fun setStartMode(mode: IntroMode) {
        _mode.value = mode
    }

    fun setIsVerifyTimeOut(isTimeOut: Boolean) {
        _isVerifyTimeOut.value = isTimeOut
    }

    fun setVerifyTime(time: String) {
        _verifyTime.value = time
    }

    fun sendVerifyCodeEvent(verifyCodeUiEvent: VerifyCodeUiEvent) {
        viewModelScope.launch {
            _verifyCodeEventFlow.emit(verifyCodeUiEvent)
        }
    }

    fun setIsUserRegistered(isUserRegistered: Boolean) {
        _isUserRegistered.value = isUserRegistered
    }

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    fun sendVerifyCode(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            startPhoneVerificationUseCase(phoneNumber, activity)
        }
    }

    fun resendVerifyCode(activity: Activity) {
        viewModelScope.launch {
            resendPhoneVerificationUseCase(activity)
        }
    }

    fun verifyCode(verifyCode: String) {
        verifyCodeUseCase(verifyCode)
    }

    override fun onVerificationSuccess(uid: String) {
        sendVerifyCodeEvent(VerifyCodeUiEvent.SUCCESS)
    }

    override fun onVerificationFailed(message: String) {
        Log.e(TAG, message)
    }

    override fun onVerifyCodeInvalid() {
        sendVerifyCodeEvent(VerifyCodeUiEvent.FAILURE)
    }

    override fun onVerificationCompleted(verifyCode: String) {
        //
    }

    override fun onCodeSent(
        verificationId: String?,
        token: PhoneAuthProvider.ForceResendingToken?,
    ) {
        //
    }

    companion object {
        const val INITIAL_VERIFY_TIME = "2:00"
    }
}

enum class IntroMode {
    LOGIN, SIGNUP
}

enum class VerifyCodeUiEvent {
    RESEND, TIMEOUT, SUCCESS, FAILURE
}
