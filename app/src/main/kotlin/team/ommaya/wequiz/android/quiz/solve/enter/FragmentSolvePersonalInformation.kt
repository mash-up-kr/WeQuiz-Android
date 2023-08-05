/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.enter

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentSolvePersonalInformationBinding
import team.ommaya.wequiz.android.quiz.solve.QuizSolveSharedViewModel
import team.ommaya.wequiz.android.utils.isValidInputLengthRange

@AndroidEntryPoint
class FragmentSolvePersonalInformation :
    BaseViewBindingFragment<FragmentSolvePersonalInformationBinding>(
        FragmentSolvePersonalInformationBinding::inflate
    ) {
    private val quizSolveSharedViewModel: QuizSolveSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            btnDone.setOnClickListener {
                quizSolveSharedViewModel.setAnonymousToken(etNameInput.text.toString())
                findNavController().navigate(R.id.fragmentQuizSolve)
            }
            etNameInput.doOnTextChanged { text, _, _, _ ->
                btnDone.isEnabled = isValidInputLengthRange(text.toString(), 1, 8)
            }
            tvQuizTitle.text = quizSolveSharedViewModel.quizDetail.value.title
        }
    }
}
