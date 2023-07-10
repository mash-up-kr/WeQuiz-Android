/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create.viewholder

import android.content.Context
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.databinding.ItemQuizCreateQuizBinding
import team.ommaya.wequiz.android.quiz.create.Question
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel
import team.ommaya.wequiz.android.quiz.create.adapter.AnswerAdapter

class QuestionViewHolder(
    private val binding: ItemQuizCreateQuizBinding,
    private val viewModel: QuizCreateViewModel,
    private val lifecycle: Lifecycle,
    private val context: Context,
    private val onQuestionItemClickListener: (Int, Boolean) -> Unit,
) : ViewHolder(binding.root) {

    private lateinit var answerAdapter: AnswerAdapter

    fun bind(item: Question, position: Int) {
        binding.apply {
            with(etQuizTitle) {
                hint = item.title
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        viewModel.setQuestionFocus(position)
                        onQuestionItemClickListener(position, true)
                    }
                }
                doOnTextChanged { text, _, _, _ ->
                    // TODO 타이틀 설정
                }
            }
            root.setOnClickListener {
                viewModel.setQuestionFocus(position)
                onQuestionItemClickListener(position, false)
            }
            answerAdapter = AnswerAdapter(
                viewModel,
                onAnswerAddItemClickListener = { onAnswerAddItemClickListener() },
                position,
                context,
            ).apply {
                submitList(viewModel.getAnswerList(position))
            }
            rvAnswerList.adapter = answerAdapter
        }

        lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.focusedPosition.collect { position ->
                    val isFocused = position == adapterPosition
                    with(binding) {
                        rvAnswerList.isVisible = isFocused
                        ivMultipleChoice.isVisible = isFocused
                        tvMultipleChoice.isVisible = isFocused
                    }

                }
            }
        }
    }

    private fun onAnswerAddItemClickListener() {
        answerAdapter.submitList(viewModel.getAnswerList(adapterPosition))
    }
}
