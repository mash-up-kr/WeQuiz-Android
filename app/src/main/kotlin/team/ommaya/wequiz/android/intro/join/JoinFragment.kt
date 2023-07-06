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
import androidx.navigation.fragment.findNavController
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentJoinBinding
import team.ommaya.wequiz.android.utils.KeyboardVisibilityUtils
import team.ommaya.wequiz.android.utils.setJoinNextButtonEnable

class JoinFragment : BaseViewBindingFragment<FragmentJoinBinding>(FragmentJoinBinding::inflate) {
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initKeyboardVisibilityUtils()
    }

    private fun initView() {
        binding.apply {
            setJoinNextButtonEnable(btnJoinNext, etJoinInputNickname, etJoinInputIntroduction)

            etJoinInputNickname.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    textInputLayoutJoinInputNickname.isCounterEnabled = true
                    textInputLayoutJoinInputIntroduction.isCounterEnabled = false
                } else {
                    textInputLayoutJoinInputNickname.isCounterEnabled = false
                }
            }

            etJoinInputIntroduction.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    textInputLayoutJoinInputNickname.isCounterEnabled = false
                    textInputLayoutJoinInputIntroduction.isCounterEnabled = true
                } else {
                    textInputLayoutJoinInputIntroduction.isCounterEnabled = false
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

    private fun initKeyboardVisibilityUtils() {
        // 임시 코드..
        // button의 style 바꾸는게 쉽지 않아서 일단 구현해놨는데 custom view로 변경 해야 할듯
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
            }
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
}
