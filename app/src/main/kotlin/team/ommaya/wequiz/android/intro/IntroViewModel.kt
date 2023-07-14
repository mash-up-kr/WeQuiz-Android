/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class IntroViewModel : ViewModel() {
    private val _mode: MutableStateFlow<IntroMode> = MutableStateFlow(IntroMode.Login)
    val mode = _mode.asStateFlow()

    private val _isVerificationSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isVerificationSucceed = _isVerificationSucceed.asStateFlow()

    fun setStartMode(mode: IntroMode) {
        _mode.value = mode
    }

    fun setVerificationSucceed(isSucceed: Boolean) {
        _isVerificationSucceed.value = isSucceed
    }
}

enum class IntroMode {
    Login, SignUp
}
