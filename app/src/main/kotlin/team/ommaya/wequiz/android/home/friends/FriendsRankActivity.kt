/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home.friends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.toImmutableList
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.domain.model.rank.Rank
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizRankUseCase
import team.ommaya.wequiz.android.home.obtainToken
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.noRippleClickable
import team.ommaya.wequiz.android.utils.toast
import javax.inject.Inject

@AndroidEntryPoint
class FriendsRankActivity : ComponentActivity() {

    @Inject
    lateinit var getQuizRankUseCase: GetQuizRankUseCase

    private val token by lazy { obtainToken() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var quizRank by remember { mutableStateOf<Rank?>(null) }

            LaunchedEffect(token) {
                quizRank =
                    getQuizRankUseCase(token, size = 30)
                        .getOrElse { exception ->
                            toast(exception.toString())
                            quizRank
                        }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = WeQuizColor.G9.value),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        Modifier
                            .size(24.dp)
                            .fitPaint(
                                drawableId = R.drawable.ic_round_chevron_left_24,
                                colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                            )
                            .noRippleClickable(::finish),
                    )
                    BasicText(
                        text = "친구 랭킹",
                        style = WeQuizTypography.B18
                            .change(color = WeQuizColor.G2)
                            .asRememberComposeStyle(),
                    )
                }

                @Suppress("NAME_SHADOWING")
                val quizRank = quizRank

                if (quizRank != null) {
                    FriendsRank(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .verticalScroll(state = rememberScrollState()),
                        friendsRanking = remember(quizRank) {
                            quizRank
                                .rankings
                                .map { item ->
                                    Triple(item.userInfo.name, item.userInfo.id, item.score)
                                }
                                .toImmutableList()
                        },
                    )
                }
            }
        }
    }
}
