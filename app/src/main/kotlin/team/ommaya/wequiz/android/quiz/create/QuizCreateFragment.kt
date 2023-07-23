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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentQuizCreateBinding
import team.ommaya.wequiz.android.quiz.create.adapter.QuizCreateAdapter
import team.ommaya.wequiz.android.utils.WeQuizDialog
import team.ommaya.wequiz.android.utils.WeQuizDialogContents
import team.ommaya.wequiz.android.utils.WeQuizSnackbar

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectFlows()
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
                            if (questionList.value.size > QuizCreateViewModel.MIN_QUESTION_COUNT) {
                                questionDeleteDialog.show(
                                    requireActivity().supportFragmentManager,
                                    "questionDeleteDialog",
                                )
                            }
                        }
                    }
                    launch {
                        isQuizMeetRequireMeet.collect { isRequired ->
                            quizSharedViewModel.setRequiredState(isRequired)
                        }
                    }
                }

                // 문제만들기 완료 이벤트
                with(quizSharedViewModel) {
                    buttonEventState.collect { state ->
                        if (state == QuizCreateSharedViewModel.QuizCreateState.CREATE) {
                            findNavController().navigate(R.id.quizCreateFinishFragment)
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
