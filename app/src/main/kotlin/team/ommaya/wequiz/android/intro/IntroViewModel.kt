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
import team.ommaya.wequiz.android.data.repository.UnregisteredException
import team.ommaya.wequiz.android.domain.AuthCallbacksListener
import team.ommaya.wequiz.android.domain.usecase.intro.ResendPhoneVerificationUseCase
import team.ommaya.wequiz.android.domain.usecase.intro.SetAuthCallbacksUseCase
import team.ommaya.wequiz.android.domain.usecase.intro.SignUpUseCase
import team.ommaya.wequiz.android.domain.usecase.intro.StartPhoneVerificationUseCase
import team.ommaya.wequiz.android.domain.usecase.intro.VerifyCodeUseCase
import team.ommaya.wequiz.android.domain.usecase.user.GetUserInformationUseCase
import team.ommaya.wequiz.android.domain.usecase.user.GetUserUseCase
import team.ommaya.wequiz.android.domain.usecase.user.SaveTokenUseCase
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    setAuthCallbacksUseCase: SetAuthCallbacksUseCase,
    private val startPhoneVerificationUseCase: StartPhoneVerificationUseCase,
    private val resendPhoneVerificationUseCase: ResendPhoneVerificationUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val getUserInformationUseCase: GetUserInformationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
) : ViewModel(), AuthCallbacksListener {

    private val _mode: MutableStateFlow<IntroMode> = MutableStateFlow(IntroMode.LOGIN)
    val mode = _mode.asStateFlow()

    private val _isVerifyTimeOut: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isVerifyTimeOut = _isVerifyTimeOut.asStateFlow()

    private val _verifyTime: MutableStateFlow<String> = MutableStateFlow(INITIAL_VERIFY_TIME)
    val verifyTime = _verifyTime.asStateFlow()

    private val _verifyCodeEventFlow: MutableSharedFlow<VerifyCodeUiEvent> = MutableSharedFlow()
    val verifyCodeEventFlow = _verifyCodeEventFlow.asSharedFlow()

    private val _token: MutableStateFlow<String> = MutableStateFlow("")
    val token = _token.asStateFlow()

    private val _nickname: MutableStateFlow<String> = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private val _phoneNumber: MutableStateFlow<String> = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()

    private val _onCodeSentFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val onCodeSentFlow = _onCodeSentFlow.asSharedFlow()

    private val _isLogin: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLogin = _isLogin.asStateFlow()

    init {
        setAuthCallbacksUseCase(this)
            .onFailure {
                Log.e(TAG, it.message.toString())
            }
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

    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    fun checkIsLogin() {
        viewModelScope.launch {
            _isLogin.value = getUserUseCase().isLogin
        }
    }

    fun sendVerifyCodeEvent(verifyCodeUiEvent: VerifyCodeUiEvent) {
        viewModelScope.launch {
            _verifyCodeEventFlow.emit(verifyCodeUiEvent)
        }
    }

    fun sendVerifyCode(phone: String, activity: Activity) {
        viewModelScope.launch {
            startPhoneVerificationUseCase(phone, activity)
                .onSuccess {
                    _phoneNumber.value = phone
                }.onFailure {
                    Log.e(TAG, it.message.toString())
                }
        }
    }

    fun resendVerifyCode(activity: Activity) {
        viewModelScope.launch {
            resendPhoneVerificationUseCase(activity)
                .onSuccess {
                    sendVerifyCodeEvent(VerifyCodeUiEvent.RESEND)
                }.onFailure {
                    Log.e(TAG, it.message.toString())
                }
        }
    }

    fun verifyCode(verifyCode: String) {
        verifyCodeUseCase(verifyCode)
            .onFailure {
                Log.e(TAG, it.message.toString())
            }
    }

    private fun getUserInformation() {
        viewModelScope.launch {
            getUserInformationUseCase(token.value)
                .onSuccess {
                    setNickname(it.data.nickname)
                    saveTokenUseCase(token.value)
                    sendVerifyCodeEvent(VerifyCodeUiEvent.REGISTERED)
                }.onFailure {
                    if (it is UnregisteredException) {
                        sendVerifyCodeEvent(VerifyCodeUiEvent.UNREGISTERED)
                    } else {
                        Log.d(TAG, "${it.message}")
                    }
                }
        }
    }

    fun signUp(
        nickname: String,
        description: String,
    ) {
        viewModelScope.launch {
            signUpUseCase(
                token.value,
                phoneNumber.value,
                nickname,
                description,
            ).onSuccess {
                saveTokenUseCase(token.value)
            }.onFailure {
                Log.e(TAG, it.message.toString())
            }
        }
    }

    override fun onVerificationSuccess(uid: String) {
        _token.value = uid
        getUserInformation()
    }

    override fun onVerificationFailed(message: String) {
        Log.e(TAG, message)
    }

    override fun onVerifyCodeInvalid() {
        sendVerifyCodeEvent(VerifyCodeUiEvent.FAILURE)
    }

    override fun onCodeSent(
        verificationId: String?,
        token: PhoneAuthProvider.ForceResendingToken?,
    ) {
        viewModelScope.launch {
            _onCodeSentFlow.emit(true)
        }
    }

    companion object {
        const val INITIAL_VERIFY_TIME = "2:00"
    }
}

enum class IntroMode {
    LOGIN, SIGNUP
}

enum class VerifyCodeUiEvent {
    RESEND, TIMEOUT, REGISTERED, UNREGISTERED, FAILURE,
}
