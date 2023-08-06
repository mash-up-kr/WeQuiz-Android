/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentQuizSolveBinding
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetailOption
import team.ommaya.wequiz.android.utils.ProgressDialog
import team.ommaya.wequiz.android.utils.SnackbarMode
import team.ommaya.wequiz.android.utils.WeQuizSnackbar
import team.ommaya.wequiz.android.utils.setTextGradient

@AndroidEntryPoint
class FragmentQuizSolve :
    BaseViewBindingFragment<FragmentQuizSolveBinding>(FragmentQuizSolveBinding::inflate) {

    private val quizSolveSharedViewModel: QuizSolveSharedViewModel by activityViewModels()
    private val quizSolveViewModel: QuizSolveViewModel by viewModels()

    private val quizAdapter: QuizAdapter by lazy {
        QuizAdapter(
            itemClickListener = { answerId, isChecked ->
                onAnswerItemClick(answerId, isChecked)
            },
            requireContext(),
        )
    }

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectFlow()
    }

    private fun initData() {
        quizSolveViewModel.initQuiz(
            quizSolveSharedViewModel.quizDetail.value
        )
    }

    private fun initView() {
        binding.apply {
            setTextGradient(tvAnswerCount)
            tvQuizTitle.text = quizSolveSharedViewModel.quizDetail.value.title
            rvSolveAnswer.adapter = quizAdapter
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivReport.setOnClickListener {
                // 구글 폼 이동
            }
        }
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(quizSolveViewModel) {
                    launch {
                        currentAndTotalCount.collect {
                            binding.tvQuizRemaining.text = getString(
                                R.string.remaining_problem,
                                it.second,
                                it.first + 1,
                            )
                            with(binding.pbQuizPercent) {
                                max = it.second
                                progress = it.first + 1
                            }
                        }
                    }

                    launch {
                        currentQuestion.collect { currentQuestion ->
                            with(binding) {
                                tvQuestionNum.text = currentQuestion.id.toString()
                                tvQuestionScore.text = currentQuestion.score.toString()
                                tvQuestionTitle.text = currentQuestion.title
                                tvAnswerCount.text =
                                    getString(R.string.answer_count, currentQuestion.answerCounts)
                            }

                            quizAdapter.submitList(currentQuestion.options)
                        }
                    }

                    launch {
                        isAnswerSufficient.collect { isSufficient ->
                            if (isSufficient) {
                                when (hasNexQuestion()) {
                                    false -> {
                                        submitAnswer(quizSolveSharedViewModel.userToken.value)
                                    }
                                    true -> {
                                        Unit
                                    }
                                }
                            }
                        }
                    }

                    launch {
                        quizSolveUiState.collect { state ->
                            when (state) {
                                is SolveUiState.Success -> {
                                    progressDialog.dismiss()
                                    quizSolveSharedViewModel.setResult(
                                        state.result,
                                        state.rank.rankings,
                                    )
                                    val destination = if (state.rank.rankings.size < 3) {
                                        R.id.resultFragment
                                    } else {
                                        R.id.resultRankingFragment
                                    }
                                    findNavController().navigate(
                                        destination,
                                        null,
                                        NavOptions.Builder()
                                            .setPopUpTo(
                                                R.id.fragmentQuizSolveEnter,
                                                inclusive = true
                                            )
                                            .build()
                                    )
                                }
                                is SolveUiState.Fail -> {
                                    progressDialog.dismiss()
                                    WeQuizSnackbar.make(
                                        requireView(),
                                        state.message,
                                        SnackbarMode.FAILURE
                                    ).show()
                                }
                                SolveUiState.Loading -> {
                                    progressDialog.show(
                                        requireActivity().supportFragmentManager,
                                        "progress"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onAnswerItemClick(item: QuizDetailOption, isChecked: Boolean) {
        quizSolveViewModel.setCurrentAnswerList(item, isChecked)
    }
}
