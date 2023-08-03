/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import android.content.Intent
import android.content.Intent.createChooser
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingActivity
import team.ommaya.wequiz.android.databinding.ActivityQuizCreateBinding

@AndroidEntryPoint
class QuizCreateActivity :
    BaseViewBindingActivity<ActivityQuizCreateBinding>(ActivityQuizCreateBinding::inflate) {

    private val quizSharedViewModel: QuizCreateSharedViewModel by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigation()
        collectFlows()
    }

    private fun initNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.quiz_create_nav_host) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(quizSharedViewModel) {
                    launch {
                        quizCreateState.collect { state ->
                            setViewByQuizState(state)
                        }
                    }
                    launch {
                        isQuizMeetRequireMeet.collect { isRequired ->
                            binding.btnQuizNext.isEnabled = isRequired
                        }
                    }
                    launch {
                        quizLink.collect { inivitationUri ->
                            startActivity(
                                Intent(Intent.ACTION_SEND).apply {
                                    type = "text/html"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "친구가 만든 찐친고사에 도전해보세요!\n\n$inivitationUri",
                                    )
                                    createChooser(intent, "친구가 만든 찐친고사에 도전해보세요!")
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setViewByQuizState(state: QuizCreateSharedViewModel.QuizCreateState) {
        with(binding) {
            when (state) {
                QuizCreateSharedViewModel.QuizCreateState.CREATE -> {
                    tvQuizCreate.setTextColor(getColor(team.ommaya.wequiz.android.design.resource.R.color.G2))
                    ivQuizCreateBack.setOnClickListener {
                        finish()
                    }
                    with(btnQuizNext) {
                        setText(R.string.complete_quiz)
                        setOnClickListener {
                            quizSharedViewModel.setButtonEventState(QuizCreateSharedViewModel.QuizCreateState.CREATE)
                        }
                    }
                }
                QuizCreateSharedViewModel.QuizCreateState.DONE -> {
                    tvQuizCreate.isVisible = false
                    with(btnQuizNext) {
                        isEnabled = true
                        setText(R.string.share_quiz)
                        setOnClickListener {
                            quizSharedViewModel.makeQuizLink()
                        }
                    }
                    ivQuizCreateBack.setOnClickListener {
                        finish()
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}
