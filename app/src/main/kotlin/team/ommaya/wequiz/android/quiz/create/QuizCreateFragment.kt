/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentQuizCreateBinding
import team.ommaya.wequiz.android.quiz.create.adapter.QuizCreateAdapter
import team.ommaya.wequiz.android.utils.ProgressDialog
import team.ommaya.wequiz.android.utils.SnackbarMode
import team.ommaya.wequiz.android.utils.WeQuizDialog
import team.ommaya.wequiz.android.utils.WeQuizDialogContents
import team.ommaya.wequiz.android.utils.WeQuizSnackbar

@AndroidEntryPoint
class QuizCreateFragment :
    BaseViewBindingFragment<FragmentQuizCreateBinding>(FragmentQuizCreateBinding::inflate) {

    private val quizCreateViewModel: QuizCreateViewModel by viewModels()
    private val quizSharedViewModel: QuizCreateSharedViewModel by activityViewModels()

    private lateinit var questionDeleteDialog: WeQuizDialog

    private val questionDeleteSnackbar: WeQuizSnackbar by lazy {
        WeQuizSnackbar.make(
            binding.root,
            getString(R.string.delete_question_noftify),
        )
    }

    private val quizAdapter by lazy {
        QuizCreateAdapter(
            quizSharedViewModel,
            quizCreateViewModel,
            requireContext(),
            viewLifecycleOwner,
            onQuestionAddItemClickListener = {
                onQuestionAddItemClick()
            },
            onQuestionItemClickListener = { position, isEditable ->
                onQuestionItemClick(position, isEditable)
            },
        )
    }

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            binding.rvQuizList.scrollToPosition(quizAdapter.itemCount - 1)
        }
    }

    private val scroller: LinearSmoothScroller by lazy {
        object : LinearSmoothScroller(requireContext()) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_END
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectFlows()
    }

    private fun initData() {
        quizCreateViewModel.setAuthToken()
    }

    private fun initView() {
        quizSharedViewModel.setQuizCreateState(QuizCreateSharedViewModel.QuizCreateState.CREATE)
        binding.apply {
            rvQuizList.adapter = quizAdapter
            with(etQuizTitle) {
                setOnFocusChangeListener { _, isFocus ->
                    ivTitleDelete.visibility = if (isFocus) View.VISIBLE else View.GONE
                }
                doOnTextChanged { text, _, _, _ ->
                    quizCreateViewModel.setQuizTitle(text.toString())
                }
            }
            ivTitleDelete.setOnClickListener {
                etQuizTitle.text.clear()
            }
        }
        quizAdapter.registerAdapterDataObserver(adapterDataObserver)
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(quizCreateViewModel) {
                    launch {
                        questionList.collect { list ->
                            if (isQuestionListModified()) {
                                quizAdapter.submitList(list)
                            }
                            quizCreateViewModel.checkQuizRequirements()
                        }
                    }
                    launch {
                        deleteQuestionAction.collect { question ->
                            val dialogContent = WeQuizDialogContents(
                                title = getString(R.string.delete_question_text),
                                negativeBtnText = getString(R.string.negative),
                                positiveBtnText = getString(R.string.delete),
                                negativeBtnAction = {
                                    questionDeleteDialog.dismiss()
                                },
                                positiveBtnAction = {
                                    deleteQuestion(question)
                                    questionDeleteSnackbar.show()
                                    questionDeleteDialog.dismiss()
                                },
                            )
                            questionDeleteDialog = WeQuizDialog(dialogContent)
                            questionDeleteDialog.show(
                                requireActivity().supportFragmentManager,
                                "questionDeleteDialog",
                            )
                        }
                    }
                    launch {
                        isQuizMeetRequireMeet.collect { isRequired ->
                            quizSharedViewModel.setRequiredState(isRequired)
                        }
                    }
                    launch {
                        createState.collect { state ->
                            when (state) {
                                QuizCreateViewModel.CreateState.Success -> {
                                    progressDialog.dismiss()
                                    quizSharedViewModel.setQuizId(quizCreateViewModel.quizId.value)
                                    findNavController().navigate(
                                        R.id.quizCreateFinishFragment,
                                        null,
                                        NavOptions.Builder()
                                            .setPopUpTo(
                                                R.id.quizCreateFragment,
                                                inclusive = true,
                                            )
                                            .build(),
                                    )
                                }
                                QuizCreateViewModel.CreateState.Loading -> {
                                    progressDialog.show(
                                        requireActivity().supportFragmentManager,
                                        "createProgress",
                                    )
                                }
                                is QuizCreateViewModel.CreateState.Fail -> {
                                    progressDialog.dismiss()
                                    WeQuizSnackbar.make(
                                        requireView(),
                                        state.message,
                                        SnackbarMode.FAILURE,
                                    ).show()
                                }
                            }
                        }
                    }
                }

                // 문제만들기 완료 이벤트
                with(quizSharedViewModel) {
                    buttonEventState.collect { state ->
                        if (state == QuizCreateSharedViewModel.QuizCreateState.CREATE) {
                            with(quizCreateViewModel) {
                                createQuiz(
                                    binding.etQuizTitle.text.toString(),
                                    checkQuizRequirements(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        quizAdapter.unregisterAdapterDataObserver(adapterDataObserver)
    }

    private fun onQuestionAddItemClick() {
        binding.root.clearFocus()
    }

    private fun onQuestionItemClick(questionPosition: Int, isEditable: Boolean) {
        if (!isEditable) {
            binding.root.clearFocus()
            hideKeyboard()
        }
        scroller.targetPosition = questionPosition + 1
        binding.rvQuizList.layoutManager?.startSmoothScroll(scroller)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            requireActivity().window.decorView.windowToken,
            0,
        )
    }
}
