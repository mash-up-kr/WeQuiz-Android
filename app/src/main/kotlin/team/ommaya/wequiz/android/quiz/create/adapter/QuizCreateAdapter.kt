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
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizCreateAddBinding
import team.ommaya.wequiz.android.databinding.ItemQuizCreateQuizBinding
import team.ommaya.wequiz.android.quiz.create.Question
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel
import team.ommaya.wequiz.android.quiz.create.viewholder.QuestionAddViewHolder
import team.ommaya.wequiz.android.quiz.create.viewholder.QuestionViewHolder

class QuizCreateAdapter(
    private val quizViewModel: QuizCreateViewModel,
    private val context: Context,
    private val lifecycle: Lifecycle,
    private val onQuestionAddItemClickListener: () -> Unit,
    private val onQuestionItemClickListener: (Int,Boolean) -> Unit,
) : ListAdapter<Question, ViewHolder>(questionDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == Question.QuestionType.Default.typeNum) {
            QuestionViewHolder(
                ItemQuizCreateQuizBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                quizViewModel,
                lifecycle,
                context,
                onQuestionItemClickListener,
            )
        } else {
            QuestionAddViewHolder(
                ItemQuizCreateAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                quizViewModel,
                onQuestionAddItemClickListener,
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is QuestionViewHolder) {
            holder.bind(getItem(position), position)
        }
        if (holder is QuestionAddViewHolder) {
            holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.typeNum
    }

    companion object {
        val questionDiffCallback = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
                oldItem.index == newItem.index

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
                oldItem == newItem
        }
    }
}

