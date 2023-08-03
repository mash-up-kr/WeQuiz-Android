/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizSolveAnswerBinding
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetailOption

class QuizAdapter(
    private val itemClickListener: (Int) -> Unit,
) : ListAdapter<QuizDetailOption, QuizDetailViewHolder>(quizDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizDetailViewHolder {
        return QuizDetailViewHolder(
            ItemQuizSolveAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            itemClickListener,
        )
    }

    override fun onBindViewHolder(holder: QuizDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val quizDiffCallback = object : DiffUtil.ItemCallback<QuizDetailOption>() {
            override fun areItemsTheSame(
                oldItem: QuizDetailOption,
                newItem: QuizDetailOption
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: QuizDetailOption,
                newItem: QuizDetailOption
            ) = oldItem === newItem
        }
    }
}

class QuizDetailViewHolder(
    private val binding: ItemQuizSolveAnswerBinding,
    private val itemClickListener: (Int) -> Unit,
) : ViewHolder(binding.root) {

    fun bind(item: QuizDetailOption) {
        with(binding) {
            tvSolveAnswer.text = item.content
        }
    }
}