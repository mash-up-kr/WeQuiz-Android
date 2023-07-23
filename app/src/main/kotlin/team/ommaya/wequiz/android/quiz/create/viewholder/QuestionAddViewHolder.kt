/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create.viewholder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizCreateAddBinding
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel.Companion.QUESTION_ADD_POSITION

class QuestionAddViewHolder(
    private val binding: ItemQuizCreateAddBinding,
    private val viewModel: QuizCreateViewModel,
    private val onQuestionItemClickListener: () -> Unit,
) : ViewHolder(binding.root) {

    fun bind() {
        binding.root.setOnClickListener {
            with(viewModel) {
                addQuestion()
                setQuestionFocus(QUESTION_ADD_POSITION)
            }
            onQuestionItemClickListener()
        }
    }
}
