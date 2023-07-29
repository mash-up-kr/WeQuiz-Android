/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.result

import android.content.ContentValues.TAG
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentResultBinding

class ResultFragment :
    BaseViewBindingFragment<FragmentResultBinding>(FragmentResultBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        with(binding) {
            with(tvResultScore) {
                text = TEST_SCORE.toString()

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

            tvResultSubtitle.text = getString(
                R.string.result_subtitle,
                "지우",
                "매쉬업귀염둥이",
            )

            tvResultTitle.text = when (TEST_SCORE) {
                in 10..29 -> getString(R.string.result_title1)
                in 30..49 -> getString(R.string.result_title2)
                in 50..69 -> getString(R.string.result_title3)
                in 70..89 -> getString(R.string.result_title4)
                in 90..100 -> getString(R.string.result_title5)
                else -> throw IllegalArgumentException("점수 범위 초과")
            }

            when (TEST_SCORE) {
                in 10..29 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_fight)
                in 30..49 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_run)
                in 50..69 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_fight)
                in 70..89 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_jjinchin)
                in 90..100 -> ivResult.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_result_fight)
                else -> throw IllegalArgumentException("점수 범위 초과")
            }

            btnResultHome.setOnClickListener {
                Log.d(TAG, "btnResultHome")
            }

            btnResultSignUp.setOnClickListener {
                Log.d(TAG, "btnResultSignUp")
            }

            btnResultRetry.setOnClickListener {
                Log.d(TAG, "btnResultRetry")
            }

            btnResultShare.setOnClickListener {
                Log.d(TAG, "btnResultShare")
            }
        }
    }

    companion object {
        const val TEST_SCORE = 89
    }
}
