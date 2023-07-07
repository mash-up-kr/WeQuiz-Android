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

fun TextInputEditText.formatTextAsPhoneNumber() {
    addTextChangedListener(PhoneNumberFormattingTextWatcher("KR"))
}

fun TextInputEditText.setRequestVerifyCodeButtonEnable(button: Button) {
    addTextChangedListener {
        button.isEnabled = isValidPhoneNumber(text.toString())
    }
}

fun TextInputEditText.observeTextLengthForAction(action: () -> Unit) {
    addTextChangedListener {
        if (isValidVerifyCode(text.toString())) action()
    }
}

fun setJoinNextButtonEnable(
    button: Button,
    nicknameEditText: TextInputEditText,
    introductionEditText: TextInputEditText,
) {
    nicknameEditText.addTextChangedListener {
        button.isEnabled =
            isValidNickname(
                nicknameEditText.text.toString(),
            ) && isValidIntroduction(
                introductionEditText.text.toString(),
            )
    }

    introductionEditText.addTextChangedListener {
        button.isEnabled =
            isValidNickname(
                nicknameEditText.text.toString(),
            ) && isValidIntroduction(
                introductionEditText.text.toString(),
            )
    }
}

private fun isValidPhoneNumber(text: String) = text.length == 13

private fun isValidVerifyCode(text: String) = text.length == 6

private fun isValidNickname(text: String) = text.length in 1..8

private fun isValidIntroduction(text: String) = text.length in 0..30
