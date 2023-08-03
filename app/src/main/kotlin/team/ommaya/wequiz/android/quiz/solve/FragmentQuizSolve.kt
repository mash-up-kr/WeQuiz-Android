/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve

import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentQuizSolveBinding
import team.ommaya.wequiz.android.design.resource.R as DesignR

@AndroidEntryPoint
class FragmentQuizSolve :
    BaseViewBindingFragment<FragmentQuizSolveBinding>(FragmentQuizSolveBinding::inflate) {

    private val quizSolveSharedViewModel: QuizSolveSharedViewModel by activityViewModels()
    private val quizSolveViewModel: QuizSolveViewModel by viewModels()

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
        quizSolveViewModel.setQuestionCount(
            quizSolveSharedViewModel.quizDetail.value.questions.size
        )
    }

    private fun initView() {
        binding.apply {
            setTextGradient(tvAnswerCount)
            tvQuizTitle.text = quizSolveSharedViewModel.quizDetail.value.title
        }
    }

    private fun setTextGradient(textView: TextView) {
        with(textView) {
            paint.shader = LinearGradient(
                paint.measureText(text.toString()) * 0.15f,
                textSize * 0.15f,
                paint.measureText(text.toString()) * 0.85f,
                textSize * 0.85f,
                intArrayOf(
                    ContextCompat.getColor(
                        context,
                        DesignR.color.S1_G_start,
                    ),
                    ContextCompat.getColor(
                        context,
                        DesignR.color.S1_G_center,
                    ),
                    ContextCompat.getColor(
                        context,
                        DesignR.color.S1_G_end,
                    ),
                ),
                null,
                Shader.TileMode.CLAMP,
            )

            val matrix = Matrix()
            matrix.setRotate(
                275f,
                paint.measureText(text.toString()) / 2,
                textSize / 2,
            )
            paint.shader.setLocalMatrix(matrix)
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
                                it.first,
                            )
                            with(binding.pbQuizPercent) {
                                max = it.second
                                progress = it.first
                            }
                        }
                    }
                }
            }
        }
    }
}