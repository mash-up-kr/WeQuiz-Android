/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
                onQuestionAddItemClickListener()
            }
        )
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            binding.rvQuizList.scrollToPosition(positionStart)
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
        }
        quizAdapter.registerAdapterDataObserver(adapterDataObserver)
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                quizCreateViewModel.questionList.collect { list ->
                    quizAdapter.submitList(list)
                    Log.d("리스트", "collectFlows: $list")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        quizAdapter.unregisterAdapterDataObserver(adapterDataObserver)
    }

    private fun onQuestionAddItemClickListener() {
        binding.root.clearFocus()
    }
}
