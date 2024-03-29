/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.result

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingFragment
import team.ommaya.wequiz.android.databinding.FragmentResultRankingBinding
import team.ommaya.wequiz.android.home.main.HomeMainActivity
import team.ommaya.wequiz.android.intro.IntroActivity
import team.ommaya.wequiz.android.quiz.solve.QuizSolveSharedViewModel
import team.ommaya.wequiz.android.quiz.solve.result.adapter.ResultRankingAdapter
import team.ommaya.wequiz.android.utils.getResultContext
import team.ommaya.wequiz.android.utils.getResultImage
import team.ommaya.wequiz.android.utils.setTextGradient

@AndroidEntryPoint
class ResultRankingFragment :
    BaseViewBindingFragment<FragmentResultRankingBinding>(FragmentResultRankingBinding::inflate) {

    private val quizSolveSharedViewModel: QuizSolveSharedViewModel by activityViewModels()

    private val resultRankingAdapter: ResultRankingAdapter by lazy {
        ResultRankingAdapter()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            requireActivity().finish()
        }
    }

    private lateinit var homeIntent: Intent

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
        homeIntent = if (quizSolveSharedViewModel.isLogin.value) {
            Intent(requireActivity(), HomeMainActivity::class.java)
        } else {
            Intent(requireActivity(), IntroActivity::class.java)
        }
    }

    private fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        with(binding) {
            setTextGradient(tvResultScore)
            rvResultRanking.adapter = resultRankingAdapter
            btnResultHome.setOnClickListener {
                startActivity(homeIntent)
                requireActivity().finish()
            }

            btnResultSignUp.setOnClickListener {
                startActivity(homeIntent)
                requireActivity().finish()
            }

            btnResultRetry.setOnClickListener {
                findNavController().navigate(
                    R.id.fragmentQuizSolveEnter,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(
                            R.id.fragmentQuizSolveEnter,
                            inclusive = true,
                        )
                        .build(),
                )
            }

            btnResultShare.setOnClickListener {
                quizSolveSharedViewModel.makeQuizLink()
            }
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    quizSolveSharedViewModel.result.collect { result ->
                        with(binding) {
                            tvResultScore.text = result.score.toString()
                            tvResultSubtitle.text = getString(
                                R.string.result_subtitle,
                                result.resolverName,
                                result.creatorName,
                            )
                            tvResultTitle.text = getResultContext(requireContext(), result.score)
                            ivResult.setImageResource(getResultImage(result.score))
                        }
                    }
                }
                launch {
                    quizSolveSharedViewModel.rankingList.collect { rankingList ->
                        resultRankingAdapter.submitList(rankingList)
                    }
                }
                launch {
                    quizSolveSharedViewModel.quizLink.collect { invitationUri ->
                        startActivity(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/html"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "친구가 만든 찐친고사에 도전해보세요!\n\n$invitationUri",
                                )
                                Intent.createChooser(
                                    requireActivity().intent,
                                    "친구가 만든 찐친고사에 도전해보세요!",
                                )
                            },
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }
}
