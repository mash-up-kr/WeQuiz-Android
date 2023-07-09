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
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.base.BaseViewBindingActivity
import team.ommaya.wequiz.android.databinding.ActivityQuizCreateBinding
import team.ommaya.wequiz.android.quiz.create.adapter.QuizCreateAdapter

class QuizCreateActivity :
    BaseViewBindingActivity<ActivityQuizCreateBinding>(ActivityQuizCreateBinding::inflate) {

    private val quizAdapter by lazy {
        QuizCreateAdapter(
            quizCreateViewModel,
            this,
            lifecycle,
            onFocusClear = {
                clearFocus()
            }
        )
    }

    private val quizCreateViewModel: QuizCreateViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        collectFlows()
    }

    private fun initView() {
        binding.apply {
            rvQuizList.adapter = quizAdapter
        }
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

    private fun clearFocus() {
        binding.root.clearFocus()
    }
}
