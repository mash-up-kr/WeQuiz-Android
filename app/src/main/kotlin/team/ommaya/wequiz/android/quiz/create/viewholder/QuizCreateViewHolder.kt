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

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import team.ommaya.wequiz.android.databinding.ItemQuizCreateQuizBinding
import team.ommaya.wequiz.android.quiz.create.Quiz

class QuizCreateViewHolder(
    private val binding: ItemQuizCreateQuizBinding,
    private val onCreateItemClickListener: (Quiz) -> Unit,
) : ViewHolder(binding.root) {

    fun bind(item: Quiz) {
        binding.apply {
            etQuizTitle.hint = item.title
            etQuizTitle.setOnFocusChangeListener { _, isFocus ->
                if (isFocus) {
                    onCreateItemClickListener(item)
                }
            }
        }
    }
}