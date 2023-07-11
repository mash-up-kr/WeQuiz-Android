/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.verifycode

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import team.ommaya.wequiz.android.base.BaseViewBindingDialogFragment
import team.ommaya.wequiz.android.databinding.DialogSignUpBinding

class SignUpDialog :
    BaseViewBindingDialogFragment<DialogSignUpBinding>(DialogSignUpBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding) {
            btnSignUpCancel.setOnClickListener {
                // PhoneFragment로 전환
                dismiss()
            }

            btnSignUp.setOnClickListener {
                // 회원가입 모드 JoinFragment로 전환
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
        )
    }
}
