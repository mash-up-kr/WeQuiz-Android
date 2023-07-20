/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

class HomeMainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) =
        ComposeView(requireActivity())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    // TODO
                    /*HomeMainScreen(
                        modifier = Modifier.fillMaxSize(),
                        nickname = "",
                        profileMessage = "",
                        profileImageSrc =,
                        friendsRanking =,
                        quizs =,
                        onQuizCreateClick = {}
                    )*/
                }
            }
}
