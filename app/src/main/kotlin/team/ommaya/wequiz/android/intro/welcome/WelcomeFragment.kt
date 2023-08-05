/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.intro.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentWelcomeBinding
import team.ommaya.wequiz.android.home.main.HomeMainActivity
import team.ommaya.wequiz.android.intro.IntroViewModel

@AndroidEntryPoint
class WelcomeFragment :
    BaseViewBindingFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {
    private val introViewModel: IntroViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        startHomeMainActivity()
    }

    private fun initView() {
        with(binding) {
            tvWelcomeTitle.text = getString(R.string.welcome_comment, introViewModel.nickname.value)
        }
    }

    private fun startHomeMainActivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(DELAY_TIME)
            startActivity(Intent(context, HomeMainActivity::class.java))
        }
    }

    companion object {
        const val DELAY_TIME = 1000L
    }
}
