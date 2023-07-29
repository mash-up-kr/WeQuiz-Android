/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.result.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import team.ommaya.wequiz.android.databinding.ItemQuizSolveRankingBinding
import team.ommaya.wequiz.android.quiz.solve.result.Ranking
import team.ommaya.wequiz.android.quiz.solve.result.viewholder.ResultRankingViewHolder

class ResultRankingAdapter :
    ListAdapter<Ranking, ResultRankingViewHolder>(rankingDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultRankingViewHolder {
        return ResultRankingViewHolder(
            ItemQuizSolveRankingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: ResultRankingViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    companion object {
        private val rankingDiffCallback = object : DiffUtil.ItemCallback<Ranking>() {
            override fun areItemsTheSame(oldItem: Ranking, newItem: Ranking): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: Ranking, newItem: Ranking): Boolean =
                oldItem == newItem
        }
    }
}
