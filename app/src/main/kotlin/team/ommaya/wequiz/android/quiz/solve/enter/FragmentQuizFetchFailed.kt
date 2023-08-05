/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.enter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentQuizFetchFailedBinding
import team.ommaya.wequiz.android.home.main.HomeMainActivity
import team.ommaya.wequiz.android.intro.IntroActivity
import team.ommaya.wequiz.android.quiz.solve.QuizSolveSharedViewModel

@AndroidEntryPoint
class FragmentQuizFetchFailed :
    BaseViewBindingFragment<FragmentQuizFetchFailedBinding>(FragmentQuizFetchFailedBinding::inflate) {

    private val quizSolveSharedViewModel: QuizSolveSharedViewModel by activityViewModels()

    private lateinit var homeIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initData() {
        homeIntent = if (quizSolveSharedViewModel.isLogin.value) {
            Intent(requireActivity(), HomeMainActivity::class.java)
        } else {
            Intent(requireActivity(), IntroActivity::class.java)
        }
    }

    private fun initView() {
        binding.ivHome.setOnClickListener {
            startActivity(homeIntent)
            requireActivity().finish()
        }
    }
}
