/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class IntroViewModel : ViewModel() {
    private val _mode: MutableStateFlow<IntroMode> = MutableStateFlow(IntroMode.Login)
    val mode = _mode.asStateFlow()

    private val _isVerificationSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isVerificationSucceed = _isVerificationSucceed.asStateFlow()

    private val _isVerifyTimeOut: MutableSharedFlow<Boolean> = MutableSharedFlow()
    var isVerifyTimeOut = _isVerifyTimeOut.asSharedFlow()

    private val _verifyTime: MutableStateFlow<String> = MutableStateFlow(VERIFY_TIME)
    val verifyTime = _verifyTime.asStateFlow()

    fun setStartMode(mode: IntroMode) {
        _mode.value = mode
    }

    fun setVerificationSucceed(isSucceed: Boolean) {
        _isVerificationSucceed.value = isSucceed
    }

    fun setVerifyTime(time: String) {
        _verifyTime.value = time
    }

    companion object {
        const val VERIFY_TIME = "3:00"
    }
}

enum class IntroMode {
    Login, SignUp
}
