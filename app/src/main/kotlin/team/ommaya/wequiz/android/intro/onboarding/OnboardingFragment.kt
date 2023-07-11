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

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentOnboardingBinding
import team.ommaya.wequiz.android.intro.IntroActivity.Companion.LOG_IN_MODE
import team.ommaya.wequiz.android.intro.IntroActivity.Companion.SIGN_UP_MODE
import team.ommaya.wequiz.android.intro.IntroViewModel

class OnboardingFragment :
    BaseViewBindingFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnOnboardingSignUp.setOnClickListener {
                introViewModel.setStartMode(SIGN_UP_MODE)
                findNavController().navigate(R.id.action_onboardingFragment_to_phoneFragment)
            }

            btnOnboardingLogin.setOnClickListener {
                introViewModel.setStartMode(LOG_IN_MODE)
                findNavController().navigate(R.id.action_onboardingFragment_to_phoneFragment)
            }
        }
    }
}
