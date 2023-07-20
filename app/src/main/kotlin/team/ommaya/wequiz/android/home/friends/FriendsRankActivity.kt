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
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.noRippleClickable

class FriendsRankActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
