/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingActivity
import team.ommaya.wequiz.android.databinding.ActivityQuizSolveBinding

@AndroidEntryPoint
class QuizSolveActivity :
    BaseViewBindingActivity<ActivityQuizSolveBinding>(ActivityQuizSolveBinding::inflate) {

    private val quizSolveViewModel: QuizSolveSharedViewModel by viewModels()


    private lateinit var navHost: NavHostFragment
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigation()
        initData()
        collectFlows()
    }

    private fun initData() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deeplink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deeplink = pendingDynamicLinkData.link
                }

                deeplink?.let {
                    quizSolveViewModel.getQuizDetail(
                        deeplink.toString().substringAfterLast("quizId=", "-1").toInt()
                    )
                }
            }
            .addOnFailureListener(this) { _ ->
                setNavGraph(false)
            }
        quizSolveViewModel.checkLogin()
    }

    private fun initNavigation() {
        navHost =
            supportFragmentManager.findFragmentById(R.id.quiz_solve_nav_host) as NavHostFragment
        navController = navHost.navController
    }

    private fun setNavGraph(isValid: Boolean) {
        val navGraph = navController.navInflater.inflate(R.navigation.quiz_solve_graph)
        navGraph.setStartDestination(if (isValid) R.id.fragmentQuizSolveEnter else R.id.fragmentQuizFetchFailed)
        navController.setGraph(navGraph, null)
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(quizSolveViewModel) {
                    isQuizValid.collect { isValid ->
                        setNavGraph(isValid)
                    }
                }
            }
        }
    }
}
