/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.base.BaseViewBindingActivity
import team.ommaya.wequiz.android.databinding.ActivityQuizSolveBinding

class QuizSolveActivity :
    BaseViewBindingActivity<ActivityQuizSolveBinding>(ActivityQuizSolveBinding::inflate) {

    private lateinit var navHost: NavHostFragment
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigation()
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deeplink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deeplink = pendingDynamicLinkData.link
                }

                deeplink?.let {
                    binding.tvUri.text = deeplink.getQueryParameter("quizId")
                }
            }
            .addOnFailureListener(this) { e -> Log.w("딥링크", "getDynamicLink:onFailure", e) }
    }

    private fun initNavigation() {
        navHost =
            supportFragmentManager.findFragmentById(R.id.quiz_solve_nav_host) as NavHostFragment
        navController = navHost.navController
    }
}
