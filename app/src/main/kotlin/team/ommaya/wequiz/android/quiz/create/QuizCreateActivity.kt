/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.base.BaseViewBindingActivity
import team.ommaya.wequiz.android.databinding.ActivityQuizCreateBinding
import team.ommaya.wequiz.android.quiz.create.adapter.QuizCreateAdapter

class QuizCreateActivity :
    BaseViewBindingActivity<ActivityQuizCreateBinding>(ActivityQuizCreateBinding::inflate) {

    private val quizCreateViewModel: QuizCreateViewModel by viewModels()

    private val quizAdapter by lazy {
        QuizCreateAdapter(
            quizCreateViewModel,
            this,
            lifecycle,
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
        object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_END
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        collectFlows()
    }

    private fun initView() {
        binding.apply {
            rvQuizList.adapter = quizAdapter
            ivQuizEdit.setOnClickListener {
                quizCreateViewModel.setEditMode()
            }
            tvQuizEidt.setOnClickListener {
                ivQuizEdit.performClick()
            }
            etQuizTitle.setOnFocusChangeListener { _, isFocus ->
                ivTitleDelete.visibility = if (isFocus) VISIBLE else GONE
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
                quizCreateViewModel.questionList.collect { list ->
                    quizAdapter.submitList(list)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            window.decorView.windowToken,
            0,
        )
    }
}
