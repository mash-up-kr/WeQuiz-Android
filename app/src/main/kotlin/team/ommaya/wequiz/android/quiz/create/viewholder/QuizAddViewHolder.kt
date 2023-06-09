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
import team.ommaya.wequiz.android.databinding.ItemQuizCreateAddBinding
import team.ommaya.wequiz.android.quiz.create.QuizCreateViewModel

class QuizAddViewHolder(
    private val binding: ItemQuizCreateAddBinding,
    private val viewModel: QuizCreateViewModel,
) : ViewHolder(binding.root) {

    fun bind() {
        binding.root.setOnClickListener {
            viewModel.addQuiz()
        }
    }
}
