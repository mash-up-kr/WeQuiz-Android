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

package team.ommaya.wequiz.android.quiz.create.viewholder

import android.content.Context
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizCreateQuizBinding
import team.ommaya.wequiz.android.quiz.create.Answer
import team.ommaya.wequiz.android.quiz.create.Quiz
import team.ommaya.wequiz.android.quiz.create.QuizAnswerAdapter

class QuizCreateViewHolder(
    private val binding: ItemQuizCreateQuizBinding,
    private val context: Context,
) : ViewHolder(binding.root) {

    private val quizAnswerAdapter by lazy {
        QuizAnswerAdapter(
            onAnswerAddItemClickListener = { onAnswerAddItemClickListener() },
            context,
        )
    }

    fun bind(item: Quiz) {
        binding.apply {
            with(etQuizTitle) {
                hint = item.title
                setOnFocusChangeListener { _, isFocus ->
                    ivTitleCancel.isVisible = isFocus
                    rvAnswerList.isVisible = true
                    ivMultipleChoice.isVisible = true
                    tvMultipleChoice.isVisible = true
                }
            }
            rvAnswerList.adapter = quizAnswerAdapter
        }
    }

    private fun onAnswerAddItemClickListener() {
        val currentSize = quizAnswerAdapter.currentList.size
        val list = mutableListOf<Answer>().apply {
            addAll(quizAnswerAdapter.currentList)
        }
        if (currentSize == 5) {
            list[currentSize - 1] =
                list.last().copy(index = currentSize - 1, type = Answer.AnswerType.Default)
        } else {
            list[list.lastIndex] =
                list.last().copy(index = list.lastIndex, type = Answer.AnswerType.Default)
            list.add(Answer(index = list.size, type = Answer.AnswerType.Add))
        }
        quizAnswerAdapter.submitList(list)
    }
}