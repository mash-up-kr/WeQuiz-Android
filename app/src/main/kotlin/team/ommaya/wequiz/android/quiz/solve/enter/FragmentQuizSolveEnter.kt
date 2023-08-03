/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.enter

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
import team.ommaya.wequiz.android.quiz.solve.QuizSolveSharedViewModel

@AndroidEntryPoint
class FragmentQuizSolveEnter :
    BaseViewBindingFragment<FragmentQuizSolveEnterBinding>(FragmentQuizSolveEnterBinding::inflate) {

    private val quizSolveSharedViewModel: QuizSolveSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
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
                            }
                        }
                    }

                    launch {
                        isLogin.collect { isLogin ->
                            binding.btnQuizStart.setOnClickListener {
                                findNavController().navigate(if (isLogin) R.id.fragmentQuizSolve else R.id.fragmentSolvePersonalInformation)
                            }
                        }
                    }
                }
            }
        }
    }
}
