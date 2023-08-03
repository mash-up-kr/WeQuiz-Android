/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.result

import android.content.ContentValues
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentResultRankingBinding
import team.ommaya.wequiz.android.design.resource.R
import team.ommaya.wequiz.android.quiz.solve.result.adapter.ResultRankingAdapter

class ResultRankingFragment :
    BaseViewBindingFragment<FragmentResultRankingBinding>(FragmentResultRankingBinding::inflate) {
    private lateinit var resultRankingAdapter: ResultRankingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initRVAdapter()
    }

    private fun initView() {
        with(binding) {
            with(tvResultScore) {
                text = ResultFragment.TEST_SCORE.toString()

                paint.shader = LinearGradient(
                    paint.measureText(text.toString()) * 0.15f,
                    textSize * 0.15f,
                    paint.measureText(text.toString()) * 0.85f,
                    textSize * 0.85f,
                    intArrayOf(
                        ContextCompat.getColor(
                            context,
                            R.color.S1_G_start,
                        ),
                        ContextCompat.getColor(
                            context,
                            R.color.S1_G_center,
                        ),
                        ContextCompat.getColor(
                            context,
                            R.color.S1_G_end,
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

            tvResultSubtitle.text = getString(
                team.ommaya.wequiz.android.R.string.result_subtitle,
                "지우",
                "매쉬업귀염둥이",
            )

            tvResultTitle.text = when (ResultFragment.TEST_SCORE) {
                in 10..29 -> getString(team.ommaya.wequiz.android.R.string.result_title1)
                in 30..49 -> getString(team.ommaya.wequiz.android.R.string.result_title2)
                in 50..69 -> getString(team.ommaya.wequiz.android.R.string.result_title3)
                in 70..89 -> getString(team.ommaya.wequiz.android.R.string.result_title4)
                in 90..100 -> getString(team.ommaya.wequiz.android.R.string.result_title5)
                else -> throw IllegalArgumentException("점수 범위 초과")
            }

            when (ResultFragment.TEST_SCORE) {
                in 10..29 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_fight)
                in 30..49 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_run)
                in 50..69 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_fight)
                in 70..89 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_jjinchin)
                in 90..100 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_fight)
                else -> throw IllegalArgumentException("점수 범위 초과")
            }

            btnResultHome.setOnClickListener {
                Log.d(ContentValues.TAG, "btnResultHome")
            }

            btnResultSignUp.setOnClickListener {
                Log.d(ContentValues.TAG, "btnResultSignUp")
            }

            btnResultRetry.setOnClickListener {
                Log.d(ContentValues.TAG, "btnResultRetry")
            }

            btnResultShare.setOnClickListener {
                Log.d(ContentValues.TAG, "btnResultShare")
            }
        }
    }

    private fun initRVAdapter() {
        val testList = listOf(
            Ranking(0, "매쉬업귀염둥이1", 123, 100),
            Ranking(1, "매쉬업귀염둥이2", 456, 90),
            Ranking(2, "매쉬업귀염둥이3", 789, 80),
            Ranking(3, "매쉬업귀염둥이4", 111, 70),
            Ranking(4, "매쉬업귀염둥이5", 222, 60),
            Ranking(5, "매쉬업귀염둥이6", 333, 50),
        )
        resultRankingAdapter = ResultRankingAdapter().apply {
            submitList(testList)
        }
        binding.rvResultRanking.adapter = resultRankingAdapter
    }
}
