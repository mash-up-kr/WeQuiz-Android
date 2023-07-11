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
import androidx.navigation.fragment.findNavController
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentPhoneBinding
import team.ommaya.wequiz.android.intro.IntroActivity.Companion.LOG_IN_MODE
import team.ommaya.wequiz.android.intro.IntroActivity.Companion.SIGN_UP_MODE
import team.ommaya.wequiz.android.intro.IntroViewModel
import team.ommaya.wequiz.android.utils.KeyboardVisibilityUtils
import team.ommaya.wequiz.android.utils.isValidInputLength

class PhoneFragment : BaseViewBindingFragment<FragmentPhoneBinding>(FragmentPhoneBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkVerificationIsSucceed()
        initView()
        initKeyboardVisibilityUtils()
    }

    private fun checkVerificationIsSucceed() {
        with(introViewModel) {
            if (isVerificationSucceed.value) {
                setVerificationSucceed(false)
                findNavController().navigate(R.id.action_phoneFragment_to_joinFragment)
            }
        }
    }

    private fun initView() {
        with(binding) {
            tvPhoneTitle.text = when (introViewModel.mode.value) {
                LOG_IN_MODE -> getString(R.string.phone_log_in_mode_title)
                SIGN_UP_MODE -> getString(R.string.phone_sign_up_mode_title)
                else -> getString(R.string.phone_log_in_mode_title)
            }

            with(etPhoneInput) {
                addTextChangedListener(PhoneNumberFormattingTextWatcher(COUNTRY_CODE_KOREA))

                addTextChangedListener {
                    btnPhoneRequestVerifyCode.isEnabled =
                        isValidInputLength(text.toString(), PHONE_NUMBER_LENGTH)
                }
            }

            btnPhoneRequestVerifyCode.setOnClickListener {
                findNavController().navigate(R.id.action_phoneFragment_to_verifyCodeFragment)
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
                params.setMargins(dpToPx(20), dpToPx(12), dpToPx(20), dpToPx(20))
                binding.btnPhoneRequestVerifyCode.layoutParams = params
                binding.etPhoneInput.clearFocus()
            },
        )
    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
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
