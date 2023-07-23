/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create.viewholder

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.databinding.ItemQuizAnswerAddBinding
import team.ommaya.wequiz.android.design.resource.R
import team.ommaya.wequiz.android.quiz.create.Question
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel

class AnswerAddViewHolder(
    private val binding: ItemQuizAnswerAddBinding,
    private val item: Question,
    private val viewModel: QuizCreateViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
) : ViewHolder(binding.root) {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun collectAnswerFlows(position: Int) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(viewModel) {
                    questionList.collect {
                        binding.apply {
                            root.setOnClickListener {
                                viewModel.addAnswer(getQuestionItemPosition(item))
                                viewModel.setQuestionFocus(getQuestionItemPosition(item))
                            }
                            val indexIconRes: Int = when (position) {
                                2 -> R.drawable.ic_index_c_empty
                                3 -> R.drawable.ic_index_d_empty
                                else -> R.drawable.ic_index_e_empty
                            }
                            ivAnswerIndex.setImageDrawable(context.getDrawable(indexIconRes))
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
