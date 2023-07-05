/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.verifycode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentVerifyCodeBinding
import team.ommaya.wequiz.android.intro.IntroViewModel
import team.ommaya.wequiz.android.utils.observeTextLengthForAction

class VerifyCodeFragment :
    BaseViewBindingFragment<FragmentVerifyCodeBinding>(FragmentVerifyCodeBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.apply {
            etVerifyCodeInput.observeTextLengthForAction {
                if (etVerifyCodeInput.text.toString() == TEST_CODE) onVerificationSucceed()
            }
        }
    }

    private fun onVerificationSucceed() {
        introViewModel.setVerificationSucceed(true)
        findNavController().popBackStack()
    }

    companion object {
        const val TEST_CODE = "123456"
    }
}
