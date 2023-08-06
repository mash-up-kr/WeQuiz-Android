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
import team.ommaya.wequiz.android.domain.model.rank.RankingsItem
import team.ommaya.wequiz.android.quiz.solve.result.viewholder.ResultRankingViewHolder

class ResultRankingAdapter :
    ListAdapter<RankingsItem, ResultRankingViewHolder>(rankingDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultRankingViewHolder {
        return ResultRankingViewHolder(
            ItemQuizSolveRankingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: ResultRankingViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        private val rankingDiffCallback = object : DiffUtil.ItemCallback<RankingsItem>() {
            override fun areItemsTheSame(oldItem: RankingsItem, newItem: RankingsItem): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: RankingsItem, newItem: RankingsItem): Boolean =
                oldItem == newItem
        }
    }
}
