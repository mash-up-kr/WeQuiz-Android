/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.join

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentJoinBinding
import team.ommaya.wequiz.android.utils.KeyboardVisibilityUtils
import team.ommaya.wequiz.android.utils.isValidInputLengthRange

class JoinFragment : BaseViewBindingFragment<FragmentJoinBinding>(FragmentJoinBinding::inflate) {
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initKeyboardVisibilityUtils()
    }

    private fun initView() {
        with(binding) {
            etJoinInputNickname.addTextChangedListener {
                setNextButtonEnable()
            }

            etJoinInputIntroduction.addTextChangedListener {
                setNextButtonEnable()
            }

            etJoinInputNickname.setOnFocusChangeListener { _, hasFocus ->
                textInputLayoutJoinInputNickname.isCounterEnabled = hasFocus
                if (hasFocus) {
                    textInputLayoutJoinInputIntroduction.isCounterEnabled = false
                }
            }

            etJoinInputIntroduction.setOnFocusChangeListener { _, hasFocus ->
                textInputLayoutJoinInputIntroduction.isCounterEnabled = hasFocus
                if (hasFocus) {
                    textInputLayoutJoinInputNickname.isCounterEnabled = false
                }
            }

            btnJoinNext.setOnClickListener {
                findNavController().navigate(R.id.action_joinFragment_to_welcomeFragment)
            }

            btnJoinBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setNextButtonEnable() {
        with(binding) {
            btnJoinNext.isEnabled =
                isValidInputLengthRange(
                    etJoinInputNickname.text.toString(),
                    MIN_NICKNAME_LENGTH,
                    MAX_NICKNAME_LENGTH,
                ) && isValidInputLengthRange(
                    etJoinInputIntroduction.text.toString(),
                    MIN_INTRODUCTION_LENGTH,
                    MAX_INTRODUCTION_LENGTH,
                )
        }
    }

    private fun initKeyboardVisibilityUtils() {
        val params = binding.btnJoinNext.layoutParams as ViewGroup.MarginLayoutParams

        keyboardVisibilityUtils = KeyboardVisibilityUtils(
            window = requireActivity().window,
            onShowKeyboard = {
                binding.btnJoinNext.setBackgroundResource(team.ommaya.wequiz.android.design.resource.R.color.selector_bottom_btn_text_color)
                params.setMargins(0, 0, 0, 0)
                binding.btnJoinNext.layoutParams = params
            },
            onHideKeyboard = {
                binding.btnJoinNext.setBackgroundResource(team.ommaya.wequiz.android.design.resource.R.drawable.btn_radius_12)
                params.setMargins(dpToPx(20), dpToPx(12), dpToPx(20), dpToPx(20))
                binding.btnJoinNext.layoutParams = params
                binding.etJoinInputNickname.clearFocus()
                binding.etJoinInputIntroduction.clearFocus()
                binding.textInputLayoutJoinInputNickname.isCounterEnabled = false
                binding.textInputLayoutJoinInputIntroduction.isCounterEnabled = false
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
        const val MIN_NICKNAME_LENGTH = 1
        const val MAX_NICKNAME_LENGTH = 8
        const val MIN_INTRODUCTION_LENGTH = 0
        const val MAX_INTRODUCTION_LENGTH = 30
    }
}
