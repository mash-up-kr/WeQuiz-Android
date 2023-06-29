/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import android.telephony.PhoneNumberFormattingTextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.phoneNumberFormatting() {
    addTextChangedListener(PhoneNumberFormattingTextWatcher())
}

fun TextInputLayout.isCounterEnabled(isEnabled: Boolean) {
    isCounterEnabled = isEnabled
}
