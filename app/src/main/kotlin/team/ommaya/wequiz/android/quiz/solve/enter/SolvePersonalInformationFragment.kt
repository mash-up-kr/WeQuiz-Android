/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.enter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentSolvePersonalInformationBinding
import team.ommaya.wequiz.android.intro.IntroActivity
import team.ommaya.wequiz.android.quiz.solve.QuizSolveSharedViewModel
import team.ommaya.wequiz.android.utils.ProgressDialog
import team.ommaya.wequiz.android.utils.SnackbarMode
import team.ommaya.wequiz.android.utils.WeQuizSnackbar
import team.ommaya.wequiz.android.utils.isValidInputLengthRange

@AndroidEntryPoint
class SolvePersonalInformationFragment :
    BaseViewBindingFragment<FragmentSolvePersonalInformationBinding>(
        FragmentSolvePersonalInformationBinding::inflate,
    ) {
    private val quizSolveSharedViewModel: QuizSolveSharedViewModel by activityViewModels()
    private val quizSolveAnonymousViewModel: QuizSolveAnonymousViewModel by viewModels()

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectFlows()
    }

    private fun initView() {
        binding.apply {
            btnDone.setOnClickListener {
                quizSolveAnonymousViewModel.getAnonymousToken(etNameInput.text.toString())
            }
            etNameInput.doOnTextChanged { text, _, _, _ ->
                btnDone.isEnabled = isValidInputLengthRange(text.toString(), 1, 8)
            }
            tvQuizTitle.text = quizSolveSharedViewModel.quizDetail.value.title
            ivCancelName.setOnClickListener {
                findNavController().popBackStack()
            }
            cvAdJoinText.setOnClickListener {
                startActivity(Intent(requireActivity(), IntroActivity::class.java))
            }
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                quizSolveAnonymousViewModel.anonymousUiState.collect { state ->
                    when (state) {
                        is QuizSolveAnonymousViewModel.UiState.Success -> {
                            progressDialog.dismiss()
                            quizSolveSharedViewModel.setToken(state.token)
                            findNavController().navigate(
                                R.id.fragmentQuizSolve,
                                null,
                                NavOptions.Builder()
                                    .setPopUpTo(R.id.fragmentQuizSolveEnter, false)
                                    .build(),
                            )
                        }
                        is QuizSolveAnonymousViewModel.UiState.Fail -> {
                            progressDialog.dismiss()
                            WeQuizSnackbar.make(requireView(), state.message, SnackbarMode.FAILURE)
                                .show()
                        }
                        QuizSolveAnonymousViewModel.UiState.Loading -> {
                            progressDialog.show(
                                requireActivity().supportFragmentManager,
                                "progressAnonymousToken",
                            )
                        }
                    }
                }
            }
        }
    }
}
