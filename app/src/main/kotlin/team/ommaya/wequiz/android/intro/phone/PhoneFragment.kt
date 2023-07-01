/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.phone

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentPhoneBinding
import team.ommaya.wequiz.android.utils.formatTextAsPhoneNumber
import team.ommaya.wequiz.android.utils.setRequestVerifyCodeButtonEnable

class PhoneFragment : BaseViewBindingFragment<FragmentPhoneBinding>(FragmentPhoneBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etPhoneInput.apply {
            formatTextAsPhoneNumber()
            setRequestVerifyCodeButtonEnable(button = binding.btnPhoneRequestVerifyCode)
        }

        binding.btnPhoneRequestVerifyCode.setOnClickListener {
            findNavController().navigate(R.id.action_phoneFragment_to_verifyCodeFragment)
        }
    }
}
