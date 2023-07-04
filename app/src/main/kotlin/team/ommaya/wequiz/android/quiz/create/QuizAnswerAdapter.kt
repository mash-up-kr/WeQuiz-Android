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

package team.ommaya.wequiz.android.quiz.create

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizAnswerAddBinding
import team.ommaya.wequiz.android.databinding.ItemQuizAnswerBinding
import team.ommaya.wequiz.android.quiz.create.viewholder.QuizAnswerAddViewHolder
import team.ommaya.wequiz.android.quiz.create.viewholder.QuizAnswerViewHolder

class QuizAnswerAdapter(
    private val onAnswerAddItemClickListener: () -> Unit,
    private val context: Context,
) : ListAdapter<Answer, ViewHolder>(answerDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == Quiz.QuizType.Create.typeNum) {
            QuizAnswerViewHolder(
                ItemQuizAnswerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                context,
            )
        } else {
            QuizAnswerAddViewHolder(
                ItemQuizAnswerAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onAnswerAddItemClickListener,
                context,
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is QuizAnswerViewHolder) {
            holder.bind(position)
        }
        if (holder is QuizAnswerAddViewHolder) {
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

data class Answer(
    val index: Int = 0,
    val answer: String = "",
    val isAnswer: Boolean = false,
    val type: AnswerType = AnswerType.Default,
) {
    sealed interface AnswerType {
        val typeNum: Int

        object Default : AnswerType {
            override val typeNum: Int = 0
        }

        object Add : AnswerType {
            override val typeNum: Int = 1
        }
    }
}
