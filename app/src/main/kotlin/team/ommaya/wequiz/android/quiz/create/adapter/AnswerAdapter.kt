/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizAnswerAddBinding
import team.ommaya.wequiz.android.databinding.ItemQuizAnswerBinding
import team.ommaya.wequiz.android.quiz.create.Answer
import team.ommaya.wequiz.android.quiz.create.Question
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel
import team.ommaya.wequiz.android.quiz.create.viewholder.AnswerAddViewHolder
import team.ommaya.wequiz.android.quiz.create.viewholder.AnswerViewHolder

class AnswerAdapter(
    private val viewModel: QuizCreateViewModel,
    private val onAnswerAddItemClickListener: () -> Unit,
    private val quizPosition: Int,
    private val context: Context
) : ListAdapter<Answer, ViewHolder>(answerDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == Question.QuestionType.Default.typeNum) {
            AnswerViewHolder(
                ItemQuizAnswerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                viewModel,
                quizPosition,
                context,
            )
        } else {
            AnswerAddViewHolder(
                ItemQuizAnswerAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                viewModel,
                onAnswerAddItemClickListener,
                quizPosition,
                context,
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is AnswerViewHolder) {
            holder.bind(position)
        }
        if (holder is AnswerAddViewHolder) {
            holder.bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.typeNum
    }

    companion object {
        val answerDiffCallback = object : DiffUtil.ItemCallback<Answer>() {
            override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean =
                oldItem.index == newItem.index

            override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean =
                oldItem == newItem
        }
    }
}
