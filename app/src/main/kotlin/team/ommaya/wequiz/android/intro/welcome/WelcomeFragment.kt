/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.welcome

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentWelcomeBinding

@AndroidEntryPoint
class WelcomeFragment :
    BaseViewBindingFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
