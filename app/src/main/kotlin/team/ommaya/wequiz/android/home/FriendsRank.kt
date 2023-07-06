/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.utils.applyIf
import team.ommaya.wequiz.android.utils.asLoose
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.get

typealias NicknameUuidScoreTriple = Triple<String, Int, Int>

private const val FriendRankingIconLayoutId = "FriendRankingIconLayout"
private const val FriendNicknameLayoutId = "FriendNicknameLayout"
private const val FriendUuidLayoutId = "FriendUuidLayout"
private const val FriendScoreLayoutId = "FriendScoreLayout"

@Composable
fun FriendsRanking(
    modifier: Modifier = Modifier,
    friendsRanking: ImmutableList<NicknameUuidScoreTriple>,
    onFriendClick: () -> Unit = {},
) {
    val roundedCornerShape4 = remember { RoundedCornerShape(4.dp) }
    val roundedCornerShape16 = remember { RoundedCornerShape(16.dp) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(friendsRanking) { rankNumber, (nickname, uuid, score) ->
            @Suppress("NAME_SHADOWING")
            val rankNumber = rankNumber + 1

            @Suppress("RememberReturnType")
            remember(nickname) {
                check(nickname.length in 1..8) { "닉네임 길이가 1..8 이여야 합니다." }
            }

            Layout(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(roundedCornerShape16)
                    .background(
                        color = WeQuizColor.G8.value,
                        shape = roundedCornerShape16,
                    )
                    .clickable(onClick = onFriendClick),
                content = {
                    Box(
                        modifier = Modifier
                            .layoutId(FriendRankingIconLayoutId)
                            .size(24.dp)
                            .applyIf(rankNumber < 4) {
                                fitPaint(
                                    drawableId = when (rankNumber) {
                                        1 -> R.drawable.ic_color_grade_gold_24
                                        2 -> R.drawable.ic_color_grade_silver_24
                                        3 -> R.drawable.ic_color_grade_bronze_24
                                        else -> throw IndexOutOfBoundsException()
                                    },
                                )
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        @Suppress("KotlinConstantConditions")
                        if (rankNumber >= 4) {
                            BasicText(
                                text = "$rankNumber",
                                style = WeQuizTypography.B16
                                    .change(color = WeQuizColor.White)
                                    .asRememberComposeStyle(),
                            )
                        }
                    }
                    BasicText(
                        modifier = Modifier.layoutId(FriendNicknameLayoutId),
                        text = nickname,
                        style = WeQuizTypography.R16
                            .change(color = WeQuizColor.G2)
                            .asRememberComposeStyle(),
                        overflow = TextOverflow.Ellipsis,
                    )
                    Box(
                        modifier = Modifier
                            .layoutId(FriendUuidLayoutId)
                            .background(
                                color = WeQuizColor.Black.value,
                                shape = roundedCornerShape4,
                            )
                            .padding(4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        BasicText(
                            text = "#$uuid",
                            style = WeQuizTypography.R10
                                .change(color = WeQuizColor.White)
                                .asRememberComposeStyle(),
                        )
                    }
                    BasicText(
                        modifier = Modifier.layoutId(FriendScoreLayoutId),
                        text = "${score}점",
                        style = WeQuizTypography.M18
                            .change(color = WeQuizColor.G1)
                            .asRememberComposeStyle(),
                    )
                },
            ) { measurables, constraints ->
                val rankIconMeasurable = measurables[FriendRankingIconLayoutId]
                val nicknameMeasurable = measurables[FriendNicknameLayoutId]
                val uuidMeasurable = measurables[FriendUuidLayoutId]
                val scoreMeasurable = measurables[FriendScoreLayoutId]

                val looseConstraints = constraints.asLoose(width = true, height = true)

                val rankIconPlaceable = rankIconMeasurable.measure(looseConstraints)
                val nicknamePlaceable = nicknameMeasurable.measure(looseConstraints)
                val uuidPlaceable = uuidMeasurable.measure(looseConstraints)
                val scorePlaceable = scoreMeasurable.measure(looseConstraints)

                val horizontalPadding = 16.dp.roundToPx()
                val verticalPadding = 17.dp.roundToPx()
                val badgeAndNicknameGap = 8.dp.roundToPx()
                val nicknameAndUuidGap = 6.dp.roundToPx()

                val height = nicknamePlaceable.height + verticalPadding * 2

                layout(width = constraints.maxWidth, height = height) {
                    rankIconPlaceable.place(
                        x = horizontalPadding,
                        y = Alignment.CenterVertically.align(
                            size = rankIconPlaceable.height,
                            space = height,
                        ),
                    )
                    nicknamePlaceable.place(
                        x = horizontalPadding + rankIconPlaceable.width + badgeAndNicknameGap,
                        y = Alignment.CenterVertically.align(
                            size = nicknamePlaceable.height,
                            space = height,
                        ),
                    )
                    uuidPlaceable.place(
                        x = horizontalPadding + rankIconPlaceable.width + badgeAndNicknameGap + nicknamePlaceable.width + nicknameAndUuidGap,
                        y = Alignment.CenterVertically.align(
                            size = uuidPlaceable.height,
                            space = height,
                        ),
                    )
                    scorePlaceable.place(
                        x = constraints.maxWidth - horizontalPadding - scorePlaceable.width,
                        y = Alignment.CenterVertically.align(
                            size = scorePlaceable.height,
                            space = height,
                        ),
                    )
                }
            }
        }
    }
}
