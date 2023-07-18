/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IntroViewModel : ViewModel() {
    private val _mode: MutableStateFlow<IntroMode> = MutableStateFlow(IntroMode.Login)
    val mode = _mode.asStateFlow()

    private val _isVerificationSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isVerificationSucceed = _isVerificationSucceed.asStateFlow()

    private val _isVerifyTimeOut: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isVerifyTimeOut = _isVerifyTimeOut.asStateFlow()

    private val _verifyTime: MutableStateFlow<String> = MutableStateFlow(INITIAL_VERIFY_TIME)
    val verifyTime = _verifyTime.asStateFlow()

    private val _verifyCodeEventFlow: MutableSharedFlow<VerifyCodeUiEvent> = MutableSharedFlow()
    val verifyCodeEventFlow = _verifyCodeEventFlow.asSharedFlow()

    fun setStartMode(mode: IntroMode) {
        _mode.value = mode
    }

    fun setVerificationSucceed(isSucceed: Boolean) {
        _isVerificationSucceed.value = isSucceed
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

    companion object {
        const val INITIAL_VERIFY_TIME = "3:00"
    }
}

enum class IntroMode {
    Login, SignUp
}

enum class VerifyCodeUiEvent {
    Resend, TimeOut, Success, Failure
}
