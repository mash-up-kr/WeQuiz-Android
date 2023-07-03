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
import team.ommaya.wequiz.android.databinding.ItemQuizCreateAddBinding
import team.ommaya.wequiz.android.databinding.ItemQuizCreateQuizBinding
import team.ommaya.wequiz.android.quiz.create.viewholder.QuizAddViewHolder
import team.ommaya.wequiz.android.quiz.create.viewholder.QuizCreateViewHolder

class QuizCreateAdapter(
    private val quizViewModel: QuizCreateViewModel,
    private val onAnswerItemClickListener: (Int) -> Unit,
    private val context: Context,
) : ListAdapter<Quiz, ViewHolder>(quizDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == Quiz.QuizType.Create.typeNum) {
            QuizCreateViewHolder(
                ItemQuizCreateQuizBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                context,
            )
        } else {
            QuizAddViewHolder(
                ItemQuizCreateAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                quizViewModel,
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is QuizCreateViewHolder) {
            holder.bind(getItem(position))
        }
        if (holder is QuizAddViewHolder) {
            holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.typeNum
    }

    companion object {
        val quizDiffCallback = object : DiffUtil.ItemCallback<Quiz>() {
            override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz): Boolean =
                oldItem.index == newItem.index

            override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz): Boolean =
                oldItem == newItem

        }
    }
}


data class Quiz(
    val index: Int = 0,
    val title: String = "문제입력",
    val answerList: List<Answer> = emptyList(),
    val isFocusable: Boolean = false,
    val type: QuizType = QuizType.Create,
) {
    sealed interface QuizType {
        val typeNum: Int

        object Create : QuizType {
            override val typeNum = 0
        }

        object Add : QuizType {
            override val typeNum = 1
        }
    }
}
