/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create.viewholder

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizAnswerAddBinding
import team.ommaya.wequiz.android.design.resource.R
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel

class QuizAnswerAddViewHolder(
    private val binding: ItemQuizAnswerAddBinding,
    private val onAnswerAddItemClickListener: () -> Unit,
    private val context: Context,
) : ViewHolder(binding.root) {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun bind(position: Int) {
        binding.apply {
            root.setOnClickListener {
                onAnswerAddItemClickListener()
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