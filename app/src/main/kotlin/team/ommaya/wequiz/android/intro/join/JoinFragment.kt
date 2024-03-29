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
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentJoinBinding
import team.ommaya.wequiz.android.intro.IntroViewModel
import team.ommaya.wequiz.android.utils.KeyboardVisibilityUtils
import team.ommaya.wequiz.android.utils.isValidInputLengthRange
import team.ommaya.wequiz.android.utils.px

@AndroidEntryPoint
class JoinFragment : BaseViewBindingFragment<FragmentJoinBinding>(FragmentJoinBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initKeyboardVisibilityUtils()
    }

    private fun initView() {
        with(binding) {
            with(etJoinInputNickname) {
                requestFocus()
                addTextChangedListener {
                    setNextButtonEnable()
                }
            }

            WindowInsetsControllerCompat(
                requireActivity().window,
                etJoinInputNickname,
            ).show(WindowInsetsCompat.Type.ime())

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
                introViewModel.signUp(
                    nickname = etJoinInputNickname.text.toString(),
                    description = etJoinInputIntroduction.text.toString(),
                )
                introViewModel.setNickname(etJoinInputNickname.text.toString())
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
                params.setMargins(20.px, 12.px, 20.px, 20.px)
                binding.btnJoinNext.layoutParams = params
                binding.etJoinInputNickname.clearFocus()
                binding.etJoinInputIntroduction.clearFocus()
                binding.textInputLayoutJoinInputNickname.isCounterEnabled = false
                binding.textInputLayoutJoinInputIntroduction.isCounterEnabled = false
            },
        )
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
