/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create.viewholder

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.databinding.ItemQuizAnswerBinding
import team.ommaya.wequiz.android.design.resource.R
import team.ommaya.wequiz.android.quiz.create.Question
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel

class AnswerViewHolder(
    private val binding: ItemQuizAnswerBinding,
    private val item: Question,
    private val viewModel: QuizCreateViewModel,
    private val lifecycle: Lifecycle,
    private val context: Context,
) : ViewHolder(binding.root) {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun collectAnswerFlows(position: Int) {
        lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(viewModel) {
                    questionList.collect {
                        val indexIconRes: Int = when (position) {
                            0 -> R.drawable.ic_index_a
                            1 -> R.drawable.ic_index_b
                            2 -> R.drawable.ic_index_c
                            3 -> R.drawable.ic_index_d
                            else -> R.drawable.ic_index_e
                        }

                        binding.apply {
                            with(etQuizDefault) {
                                setOnFocusChangeListener { _, isFocus ->
                                    ivAnswerTitleDelete.isVisible = isFocus
                                }
                            }
                            root.setOnClickListener {
                                viewModel.setQuestionFocus(getQuestionItemPosition(item))
                            }
                            ivAnswerIndex.setImageDrawable(context.getDrawable(indexIconRes))
                            ivAnswerTitleDelete.setOnClickListener {
                                etQuizDefault.text.clear()
                            }
                        }
                    }
                }
            }
        }
    }

    fun bind(position: Int) {
        collectAnswerFlows(position)
    }
}
