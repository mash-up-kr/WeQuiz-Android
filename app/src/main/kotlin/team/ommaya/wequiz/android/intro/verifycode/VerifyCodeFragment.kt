/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.verifycode

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentVerifyCodeBinding
import team.ommaya.wequiz.android.intro.IntroViewModel
import team.ommaya.wequiz.android.utils.isValidInputLength

class VerifyCodeFragment :
    BaseViewBindingFragment<FragmentVerifyCodeBinding>(FragmentVerifyCodeBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        with(binding.etVerifyCodeInput) {
            addTextChangedListener {
                if (isValidInputLength(text.toString(), VERIFY_CODE_LENGTH)) {
                    if (text.toString() == TEST_VERIFY_CODE) {
                        onVerificationSucceed()
                    } else {
                        // 인증번호가 올바르지 않아요 snack bar trigger
                    }
                }
            }
        }
    }

    private fun onVerificationSucceed() {
        introViewModel.setVerificationSucceed(true)
        findNavController().popBackStack()
    }

    companion object {
        const val VERIFY_CODE_LENGTH = 6
        const val TEST_VERIFY_CODE = "123456"
    }
}
