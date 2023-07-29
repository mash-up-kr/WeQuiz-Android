/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View.GONE
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.databinding.ItemQuizCreateQuizBinding
import team.ommaya.wequiz.android.design.resource.R
import team.ommaya.wequiz.android.quiz.create.Question
import team.ommaya.wequiz.android.quiz.create.QuizCreateSharedViewModel
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel
import team.ommaya.wequiz.android.quiz.create.adapter.AnswerAdapter

class QuestionViewHolder(
    private val binding: ItemQuizCreateQuizBinding,
    private val quizSharedViewModel: QuizCreateSharedViewModel,
    private val viewModel: QuizCreateViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val onQuestionItemClickListener: (Int, Boolean) -> Unit,
) : ViewHolder(binding.root) {

    private lateinit var answerAdapter: AnswerAdapter

    fun bind(item: Question, position: Int) {
        binding.apply {
            answerAdapter = AnswerAdapter(
                viewModel,
                lifecycleOwner,
                item,
                context,
            ).apply {
                submitList(viewModel.getAnswerList(position))
            }
            rvAnswerList.adapter = answerAdapter
        }

        collectFlows(item)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun collectFlows(item: Question) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.questionList.collect {
                        val currentItem = viewModel.getSyncedQuestion(item)
                        val position = viewModel.getQuestionItemPosition(item)
                        with(binding) {
                            rvAnswerList.isVisible = currentItem.isFocus
                            ivMultipleChoice.isVisible = currentItem.isFocus
                            tvMultipleChoice.isVisible = currentItem.isFocus
                            ivDeleteQuestion.isVisible = currentItem.answerList.size > 3

                            val multipleChoiceIconRes = if (currentItem.isMultipleChoice) {
                                R.drawable.ic_circle_success
                            } else {
                                R.drawable.ic_checkmark
                            }

                            ivMultipleChoice.setImageDrawable(
                                context.getDrawable(multipleChoiceIconRes),
                            )
                        }
                        answerAdapter.submitList(viewModel.getAnswerList(position))
                        initListener(item)
                    }
                }
            }
        }
    }

    private fun initListener(item: Question) {
        with(viewModel) {
            with(binding) {
                with(etQuizTitle) {
                    hint = item.title
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            setQuestionFocus(getSyncedQuestionPosition(item))
                            onQuestionItemClickListener(
                                getSyncedQuestionPosition(item),
                                true,
                            )
                        }
                        ivQuestionTitleDelete.visibility = if (hasFocus) VISIBLE else GONE
                    }
                    doOnTextChanged { text, _, _, _ ->
                        setQuestionTitle(getSyncedQuestionPosition(item), text.toString())
                    }
                }
                ivQuestionTitleDelete.setOnClickListener {
                    etQuizTitle.text.clear()
                }
                root.setOnClickListener {
                    setQuestionFocus(getSyncedQuestionPosition(item), true)
                    onQuestionItemClickListener(
                        getSyncedQuestionPosition(item),
                        false,
                    )
                }
                vMultipleChoice.setOnClickListener {
                    setMultipleChoice(getSyncedQuestionPosition(item))
                }
                ivDeleteQuestion.setOnClickListener {
                    setDeleteQuestionElement(item)
                }
            }
        }
    }
}
