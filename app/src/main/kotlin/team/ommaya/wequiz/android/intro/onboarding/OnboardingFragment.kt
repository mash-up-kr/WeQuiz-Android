/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.onboarding

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentOnboardingBinding
import team.ommaya.wequiz.android.intro.IntroMode
import team.ommaya.wequiz.android.intro.IntroViewModel

@AndroidEntryPoint
class OnboardingFragment :
    BaseViewBindingFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()
    private var statusBarColor = Color.TRANSPARENT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBarColor(true)
        setNavigationBarPadding()
        initView()
    }

    private fun setNavigationBarPadding() {
        val params = binding.fragmentOnboarding.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0, 0, 0, getNavigationBarHeight())
        binding.fragmentOnboarding.layoutParams = params
    }

    private fun getNavigationBarHeight(): Int {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    private fun setStatusBarColor(isTransparent: Boolean) {
        val window = requireActivity().window
        statusBarColor = window.statusBarColor

        if (isTransparent) {
            statusBarColor = window.statusBarColor
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.statusBarColor = statusBarColor
        }

        WindowCompat.setDecorFitsSystemWindows(window, !isTransparent)
    }

    private fun initView() {
        with(binding) {
            btnOnboardingSignUp.setOnClickListener {
                introViewModel.setStartMode(IntroMode.SIGNUP)
                findNavController().navigate(R.id.action_onboardingFragment_to_phoneFragment)
            }

            btnOnboardingLogin.setOnClickListener {
                introViewModel.setStartMode(IntroMode.LOGIN)
                findNavController().navigate(R.id.action_onboardingFragment_to_phoneFragment)
            }
        }
    }

    override fun onStop() {
        super.onStop()

        setStatusBarColor(false)
    }
}
