/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.result

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentResultBinding
import team.ommaya.wequiz.android.home.main.HomeMainActivity
import team.ommaya.wequiz.android.intro.IntroActivity
import team.ommaya.wequiz.android.quiz.solve.QuizSolveSharedViewModel
import team.ommaya.wequiz.android.design.resource.R as DesignR

@AndroidEntryPoint
class ResultFragment :
    BaseViewBindingFragment<FragmentResultBinding>(FragmentResultBinding::inflate) {

    private val quizSolveSharedViewModel: QuizSolveSharedViewModel by activityViewModels()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {}
    }

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
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        homeIntent = if (quizSolveSharedViewModel.isLogin.value) {
            Intent(requireActivity(), HomeMainActivity::class.java)
        } else {
            Intent(requireActivity(), IntroActivity::class.java)
        }
    }

    private fun initView() {
        with(binding) {
            with(tvResultScore) {
                paint.shader = LinearGradient(
                    paint.measureText(text.toString()) * 0.15f,
                    textSize * 0.15f,
                    paint.measureText(text.toString()) * 0.85f,
                    textSize * 0.85f,
                    intArrayOf(
                        ContextCompat.getColor(
                            context,
                            team.ommaya.wequiz.android.design.resource.R.color.S1_G_start,
                        ),
                        ContextCompat.getColor(
                            context,
                            team.ommaya.wequiz.android.design.resource.R.color.S1_G_center,
                        ),
                        ContextCompat.getColor(
                            context,
                            team.ommaya.wequiz.android.design.resource.R.color.S1_G_end,
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
            btnResultHome.setOnClickListener {
                startActivity(homeIntent)
                requireActivity().finish()
            }

            btnResultSignUp.setOnClickListener {
                startActivity(homeIntent)
                requireActivity().finish()
            }

            btnResultRetry.setOnClickListener {
                findNavController().navigate(
                    R.id.fragmentQuizSolveEnter,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(
                            R.id.fragmentQuizSolveEnter,
                            inclusive = true,
                        )
                        .build()
                )
            }

            btnResultShare.setOnClickListener {
                Log.d(TAG, "btnResultShare")
            }
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                quizSolveSharedViewModel.result.collect { result ->
                    with(binding) {
                        tvResultSubtitle.text = getString(
                            R.string.result_subtitle,
                            result.resolverName,
                            result.creatorName,
                        )
                        tvResultScore.text = result.score.toString()
                        tvResultTitle.text = getResultContext(result.score)
                        ivResult.setImageResource(getResultImage(result.score))
                    }
                }
            }
        }
    }

    private fun getResultContext(score: Int): String {
        return when (score) {
            in 30..49 -> getString(R.string.result_title2)
            in 50..69 -> getString(R.string.result_title3)
            in 70..89 -> getString(R.string.result_title4)
            in 90..100 -> getString(R.string.result_title5)
            else -> getString(R.string.result_title1)
        }
    }

    private fun getResultImage(score: Int): Int {
        return when (score) {
            in 30..49 -> DesignR.drawable.img_result_02
            in 50..69 -> DesignR.drawable.img_result_03
            in 70..89 -> DesignR.drawable.img_result_04
            in 90..100 -> DesignR.drawable.img_result_05
            else -> DesignR.drawable.img_result_01
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }

    companion object {
        const val TEST_SCORE = 89
    }
}
