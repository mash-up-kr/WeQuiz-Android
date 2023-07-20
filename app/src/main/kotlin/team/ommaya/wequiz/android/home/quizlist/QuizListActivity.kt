/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home.quizlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.utils.asLoose
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.get
import team.ommaya.wequiz.android.utils.noRippleClickable

private const val LeadingBackIconLayoutId = "LeadingBackIconLayout"
private const val TitleLayoutId = "TitleLayout"
private const val TrailingSearchIconLayoutId = "TrailingSearchIconLayout"

class QuizListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = WeQuizColor.G9.value),
            ) {
                Layout(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Box(
                            Modifier
                                .layoutId(LeadingBackIconLayoutId)
                                .size(24.dp)
                                .fitPaint(
                                    drawableId = R.drawable.ic_round_chevron_left_24,
                                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                                )
                                .noRippleClickable(::finish),
                        )
                        BasicText(
                            modifier = Modifier.layoutId(TitleLayoutId),
                            text = "문제 리스트",
                            style = WeQuizTypography.B18
                                .change(color = WeQuizColor.G2)
                                .asRememberComposeStyle(),
                        )
                        Box(
                            Modifier
                                .layoutId(TrailingSearchIconLayoutId)
                                .size(24.dp)
                                .fitPaint(
                                    drawableId = R.drawable.ic_outline_search_24,
                                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                                )
                                .noRippleClickable { /* TODO */ },
                        )
                    },
                ) { measurables, constraints ->
                    val looseConstraints = constraints.asLoose(width = true, height = true)

                    val backIcon = measurables[LeadingBackIconLayoutId].measure(looseConstraints)
                    val title = measurables[TitleLayoutId].measure(looseConstraints)
                    val search = measurables[TrailingSearchIconLayoutId].measure(looseConstraints)

                    val width = constraints.maxWidth
                    val height = title.height + (15.dp.roundToPx() * 2)

                    layout(width = width, height = height) {
                        backIcon.place(
                            x = 12.dp.roundToPx(),
                            y = Alignment.CenterVertically.align(
                                size = backIcon.height,
                                space = height,
                            ),
                        )
                        title.place(
                            x = 12.dp.roundToPx() + backIcon.width + 4.dp.roundToPx(),
                            y = Alignment.CenterVertically.align(
                                size = title.height,
                                space = height,
                            ),
                        )
                        search.place(
                            x = width - 20.dp.roundToPx() - search.width,
                            y = Alignment.CenterVertically.align(
                                size = search.height,
                                space = height,
                            ),
                        )
                    }
                }
                // TODO
                /*FriendsRankScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                friendsRanking = ,
                onFriendClick = {},
                )*/
            }
        }
    }
}
