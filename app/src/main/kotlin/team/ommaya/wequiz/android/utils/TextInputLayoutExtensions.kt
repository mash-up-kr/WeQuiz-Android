/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.utils

import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText

const val PHONE_NUMBER_MAX_LENGTH_KR = 13

fun TextInputEditText.formatTextAsPhoneNumber() {
    addTextChangedListener(PhoneNumberFormattingTextWatcher("KR"))
}

fun TextInputEditText.setButtonEnable(button: Button, isEnabled: Boolean) {
    button.isEnabled = isEnabled
}

fun TextInputEditText.setRequestVerifyCodeButtonEnable(button: Button) {
    addTextChangedListener {
        val text = text ?: ""
        setButtonEnable(button, text.length == PHONE_NUMBER_MAX_LENGTH_KR)
    }
}
