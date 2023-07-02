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
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizAnswerBinding
import team.ommaya.wequiz.android.design.resource.R
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel

class QuizAnswerViewHolder(
    private val binding: ItemQuizAnswerBinding,
    private val quizViewModel: QuizCreateViewModel,
    private val context: Context,
) : ViewHolder(binding.root) {


    @SuppressLint("UseCompatLoadingForDrawables")
    fun bind(position: Int) {
        binding.apply {
            etQuizDefault.setOnFocusChangeListener { v, isFocus ->
                ivAnswerClose.isVisible = isFocus
            }
            val indexIconRes: Int = when (position) {
                0 -> R.drawable.ic_index_a
                1 -> R.drawable.ic_index_b
                2 -> R.drawable.ic_index_c
                3 -> R.drawable.ic_index_d
                else -> R.drawable.ic_index_e
            }
            ivAnswerIndex.setImageDrawable(context.getDrawable(indexIconRes))
        }
    }
}