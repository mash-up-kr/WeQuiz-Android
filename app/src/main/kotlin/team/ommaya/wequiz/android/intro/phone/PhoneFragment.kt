/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.phone

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentPhoneBinding
import team.ommaya.wequiz.android.intro.IntroMode
import team.ommaya.wequiz.android.intro.IntroViewModel
import team.ommaya.wequiz.android.utils.KeyboardVisibilityUtils
import team.ommaya.wequiz.android.utils.isValidInputLength
import team.ommaya.wequiz.android.utils.px

@AndroidEntryPoint
class PhoneFragment : BaseViewBindingFragment<FragmentPhoneBinding>(FragmentPhoneBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initKeyboardVisibilityUtils()
        collectFlow()
    }

    private fun initView() {
        with(binding) {
            tvPhoneTitle.text = when (introViewModel.mode.value) {
                IntroMode.LOGIN -> getString(R.string.phone_log_in_mode_title)
                IntroMode.SIGNUP -> getString(R.string.phone_sign_up_mode_title)
            }

            with(etPhoneInput) {
                addTextChangedListener(PhoneNumberFormattingTextWatcher(COUNTRY_CODE_KOREA))

                addTextChangedListener {
                    btnPhoneRequestVerifyCode.isEnabled =
                        isValidInputLength(text.toString(), PHONE_NUMBER_LENGTH)
                }
            }

            btnPhoneRequestVerifyCode.setOnClickListener {
                introViewModel.sendVerifyCode(etPhoneInput.text.toString(), requireActivity())
            }

            btnPhoneBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initKeyboardVisibilityUtils() {
        val params = binding.btnPhoneRequestVerifyCode.layoutParams as MarginLayoutParams

        keyboardVisibilityUtils = KeyboardVisibilityUtils(
            window = requireActivity().window,
            onShowKeyboard = {
                binding.btnPhoneRequestVerifyCode.setBackgroundResource(team.ommaya.wequiz.android.design.resource.R.color.selector_bottom_btn_text_color)
                params.setMargins(0, 0, 0, 0)
                binding.btnPhoneRequestVerifyCode.layoutParams = params
            },
            onHideKeyboard = {
                binding.btnPhoneRequestVerifyCode.setBackgroundResource(team.ommaya.wequiz.android.design.resource.R.drawable.btn_radius_12)
                params.setMargins(20.px, 12.px, 20.px, 20.px)
                binding.btnPhoneRequestVerifyCode.layoutParams = params
                binding.etPhoneInput.clearFocus()
            },
        )
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    introViewModel.onCodeSentFlow.collect { isCodeSent ->
                        if (isCodeSent) {
                            binding.etPhoneInput.text?.clear()
                            findNavController().navigate(R.id.action_phoneFragment_to_verifyCodeFragment)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        keyboardVisibilityUtils.detachKeyboardListeners()
    }

    companion object {
        const val PHONE_NUMBER_LENGTH = 13
        const val COUNTRY_CODE_KOREA = "KR"
    }
}
