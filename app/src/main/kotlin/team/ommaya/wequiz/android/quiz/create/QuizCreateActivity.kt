/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import android.os.Bundle
import androidx.activity.viewModels
import team.ommaya.wequiz.android.base.BaseViewBindingActivity
import team.ommaya.wequiz.android.databinding.ActivityQuizCreateBinding
import team.ommaya.wequiz.android.quiz.create.viewholder.QuizCreateViewModel

class QuizCreateActivity :
    BaseViewBindingActivity<ActivityQuizCreateBinding>(ActivityQuizCreateBinding::inflate) {

    private val quizAdapter by lazy {
        QuizCreateAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.rvQuizList.adapter = quizAdapter
        quizAdapter.submitList(
            listOf(
                Quiz(0, "제목 1", emptyList(), Quiz.QuizType.Create),
                Quiz(1, "제목 2", emptyList(), Quiz.QuizType.Create),
                Quiz(2, "제목 3", emptyList(), Quiz.QuizType.Create),
                Quiz(3, "제목 4", emptyList(), Quiz.QuizType.Create),
                Quiz(4, "", emptyList(), Quiz.QuizType.Add),
            )
        )
    }
}