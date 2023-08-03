/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.result.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizSolveRankingBinding
import team.ommaya.wequiz.android.quiz.solve.result.Ranking

class ResultRankingViewHolder(
    private val binding: ItemQuizSolveRankingBinding,
) : ViewHolder(binding.root) {
    fun bind(ranking: Ranking, position: Int) {
        with(binding) {
            tvResultNickname.text = ranking.nickname
            tvResultHash.text = "#${ranking.hash}"
            tvResultScore.text = "${ranking.score}점"

            when (position) {
                0 -> {
                    ivResultGrade.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_grade_gold)
                }
                1 -> {
                    ivResultGrade.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_grade_silver)
                }
                2 -> {
                    ivResultGrade.setImageResource(team.ommaya.wequiz.android.design.resource.R.drawable.ic_grade_bronze)
                }
                else -> {
                    ivResultGrade.visibility = View.INVISIBLE
                    tvResultGrade.visibility = View.VISIBLE
                    tvResultGrade.text = (position + 1).toString()
                }
            }
        }
    }
}
