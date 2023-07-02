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

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizCreateQuizBinding
import team.ommaya.wequiz.android.quiz.create.Answer
import team.ommaya.wequiz.android.quiz.create.Quiz
import team.ommaya.wequiz.android.quiz.create.QuizAnswerAdapter
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel

class QuizCreateViewHolder(
    private val binding: ItemQuizCreateQuizBinding,
    private val viewModel: QuizCreateViewModel,
) : ViewHolder(binding.root) {

    private val initialList: List<Answer> = listOf(
        Answer(index = 0),
        Answer(index = 1),
        Answer(index = -1, type = Answer.AnswerType.Add)
    )
    private val quizAnswerAdapter by lazy {
        QuizAnswerAdapter(viewModel)
    }

    fun bind(item: Quiz) {
        binding.apply {
            etQuizTitle.hint = item.title
            etQuizTitle.setOnFocusChangeListener { _, isFocus ->
                ivTitleCancel.isVisible = isFocus
                rvAnswerList.isVisible = true
                ivMultipleChoice.isVisible = true
                tvMultipleChoice.isVisible = true
            }
            rvAnswerList.adapter = quizAnswerAdapter
            quizAnswerAdapter.submitList(initialList)
            root.animate().duration = 200
        }
    }
}