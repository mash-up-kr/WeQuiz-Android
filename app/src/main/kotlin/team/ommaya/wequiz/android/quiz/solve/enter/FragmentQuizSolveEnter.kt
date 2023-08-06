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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentQuizSolveEnterBinding
import team.ommaya.wequiz.android.home.main.HomeMainActivity
import team.ommaya.wequiz.android.intro.IntroActivity
import team.ommaya.wequiz.android.quiz.solve.QuizSolveSharedViewModel

@AndroidEntryPoint
class FragmentQuizSolveEnter :
    BaseViewBindingFragment<FragmentQuizSolveEnterBinding>(FragmentQuizSolveEnterBinding::inflate) {

    private val quizSolveSharedViewModel: QuizSolveSharedViewModel by activityViewModels()

    private lateinit var homeIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectFlows()
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

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(quizSolveSharedViewModel) {
                    launch {
                        quizDetail.collect { quizDetail ->
                            with(binding) {
                                tvQuizNumber.text = getString(R.string.quiz_num, quizDetail.id)
                                tvQuizTitle.text = getString(R.string.quiz_title, quizDetail.title)
                                tvQuizAuthor.text = quizDetail.creator.name
                            }
                        }
                    }

                    launch {
                        isLogin.collect { isLogin ->
                            binding.btnQuizStart.setOnClickListener {
                                val destination =
                                    if (isLogin) {
                                        R.id.fragmentQuizSolve
                                    } else if (quizSolveSharedViewModel.userToken.value.isNotBlank()) {
                                        R.id.fragmentQuizSolve
                                    } else {
                                        R.id.fragmentSolvePersonalInformation
                                    }
                                findNavController().navigate(destination)
                            }
                        }
                    }
                }
            }
        }
    }
}
