/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizSolveAnswerBinding
import team.ommaya.wequiz.android.design.resource.R
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetailOption

class QuizAdapter(
    private val itemClickListener: (QuizDetailOption, Boolean) -> Unit,
    private val context: Context,
) : ListAdapter<QuizDetailOption, QuizDetailViewHolder>(quizDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizDetailViewHolder {
        return QuizDetailViewHolder(
            ItemQuizSolveAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            context,
            itemClickListener,
        )
    }

    override fun onBindViewHolder(holder: QuizDetailViewHolder, position: Int) {
        holder.bind(getItem(position), currentList.size)
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
            ) = oldItem == newItem
        }
    }
}

class QuizDetailViewHolder(
    private val binding: ItemQuizSolveAnswerBinding,
    private val context: Context,
    private val itemClickListener: (QuizDetailOption, Boolean) -> Unit,
) : ViewHolder(binding.root) {

    private var isChecked = false

    fun bind(item: QuizDetailOption, itemCount: Int) {
        with(binding) {
            tvSolveAnswer.text = item.content
            root.setOnClickListener {
                isChecked = !isChecked
                if (isChecked) {
                    it.setBackgroundResource(getAnswerColorList(itemCount)[adapterPosition])
                    tvSolveAnswer.setTextColor(context.getColor(R.color.G9))
                } else {
                    it.setBackgroundResource(R.drawable.bg_g8_radius_16)
                    tvSolveAnswer.setTextColor(context.getColor(R.color.G2))
                }
                itemClickListener(item, isChecked)
            }
        }
    }
}

private fun getAnswerColorList(count: Int): List<Int> {
    return when (count) {
        2 -> {
            listOf(
                R.drawable.bg_s1_radius_16,
                R.drawable.bg_s5_radius_16,
            )
        }
        3 -> {
            listOf(
                R.drawable.bg_s1_radius_16,
                R.drawable.bg_s3_radius_16,
                R.drawable.bg_s5_radius_16,
            )
        }
        4 -> {
            listOf(
                R.drawable.bg_s1_radius_16,
                R.drawable.bg_s3_radius_16,
                R.drawable.bg_s4_radius_16,
                R.drawable.bg_s5_radius_16,
            )
        }
        else -> {
            listOf(
                R.drawable.bg_s1_radius_16,
                R.drawable.bg_s2_radius_16,
                R.drawable.bg_s3_radius_16,
                R.drawable.bg_s4_radius_16,
                R.drawable.bg_s5_radius_16,
            )
        }
    }
}
