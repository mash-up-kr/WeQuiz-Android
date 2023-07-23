/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentQuizCreateFinishBinding

class QuizCreateFinishFragment :
    BaseViewBindingFragment<FragmentQuizCreateFinishBinding>(FragmentQuizCreateFinishBinding::inflate) {

    private val quizSharedViewModel: QuizCreateSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quizSharedViewModel.setQuizCreateState(QuizCreateSharedViewModel.QuizCreateState.DONE)
    }
}
