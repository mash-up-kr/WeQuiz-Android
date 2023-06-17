/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography

@Suppress("unused")
@Composable
fun HomeMainScreen(
    modifier: Modifier = Modifier,
    nickname: String,
    profileMessage: String,
    profileImageSrc: Any,
    // 닉네임, 고유번호, 점수, 순서가 순위
    friendsRanking: ImmutableList<Triple<String, String, String>>,
    // 문제지명, 작성 중 여부
    examPagers: ImmutableList<Pair<String, Boolean>>,
    onExamCreateClick: () -> Unit,
) {
    val roundedCornerShape16 = remember { RoundedCornerShape(16.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WeQuizColor.G9.value)
            .padding(vertical = 27.dp, horizontal = 20.dp)
            .then(modifier),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(85.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = WeQuizColor.S3.value,
                        shape = CircleShape,
                    ),
                model = profileImageSrc,
                contentDescription = "profile_image",
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                BasicText(
                    text = nickname,
                    style = WeQuizTypography.B18.asRememberComposeStyle(),
                )
                BasicText(
                    text = profileMessage,
                    style = WeQuizTypography.M14.asRememberComposeStyle(),
                    maxLines = 2,
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 26.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(roundedCornerShape16)
                .background(color = WeQuizColor.P1.value, shape = roundedCornerShape16)
                .clickable(onClick = onExamCreateClick)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            BasicText(
                text = "문제만들기 \uD83D\uDCAC",
                style = WeQuizTypography.B16.asRememberComposeStyle(),
            )
        }
        if (friendsRanking.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                SectionTitle(title = "친구 랭킹")
                FriendsRanking(
                    friendsRanking = remember(friendsRanking) {
                        friendsRanking.take(3).toImmutableList()
                    },
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = if (friendsRanking.isEmpty()) 20.dp else 26.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            SectionTitle(title = "내가 낸 문제지")
            if (examPagers.isEmpty()) {
                CreateExamIsEmpty(
                    modifier = Modifier.padding(top = (90 - 24).dp),
                )
            } else {
                @Suppress("unused")
                CreateExams(examPagers = examPagers)
            }
        }
    }
}

@Composable
private fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String,
    onRightArrowClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BasicText(
            text = title,
            style = WeQuizTypography.B20.asRememberComposeStyle(),
        )
        Box(
            Modifier
                .size(width = 9.dp, height = 16.dp)
                .paint(
                    painter = painterResource(R.drawable.ic_round_arrow_24),
                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                )
                .clickable(
                    onClick = onRightArrowClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                )
        )
    }
}

private const val FriendRankingIconLayoutId = "FriendRankingIconLayout"
private const val FriendNicknameLayoutId = "FriendNicknameLayout"
private const val FriendUuidLayoutId = "FriendNicknameLayout"
private const val FriendScoreLayoutId = "FriendScoreLayout"

@Composable
private fun FriendsRanking(
    modifier: Modifier = Modifier,
    friendsRanking: ImmutableList<Triple<String, String, String>>,
    onFriendClick: () -> Unit = {},
) {
    val roundedCornerShape4 = remember { RoundedCornerShape(4.dp) }
    val roundedCornerShape16 = remember { RoundedCornerShape(16.dp) }

    LaunchedEffect(friendsRanking) {
        require(friendsRanking.size <= 3) {
            "친구 랭킹은 최대 3위까지만 노출 가능합니다."
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(friendsRanking) { rankNum, (nickname, uuid, scroe) ->
            Layout(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(roundedCornerShape16)
                    .background(
                        color = WeQuizColor.G8.value,
                        shape = roundedCornerShape16,
                    )
                    .clickable(onClick = onFriendClick)
                    .padding(horizontal = 16.dp),
                content = {
                    Box(
                        Modifier
                            .layoutId(FriendRankingIconLayoutId)
                            .size(20.dp)
                            .paint(
                                painter = painterResource(
                                    when (rankNum) {
                                        0 -> R.drawable.ic_round_cruelty_free_24
                                        1 -> R.drawable.ic_round_cruelty_free_24
                                        2 -> R.drawable.ic_round_cruelty_free_24
                                        else -> throw IndexOutOfBoundsException()
                                    },
                                ),
                                colorFilter = WeQuizColor.P1.toRememberColorFilterOrNull(),
                            ),
                    )
                    BasicText(
                        modifier = Modifier.layoutId(FriendNicknameLayoutId),
                        text = nickname,
                        style = WeQuizTypography.R16.asRememberComposeStyle(),
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
                        text = "${scroe}점",
                        style = WeQuizTypography.M18
                            .change(color = WeQuizColor.G1)
                            .asRememberComposeStyle(),
                    )
                },
            ) { measurables, constraints ->
                val rankIconMeasurable = measurables.fastFirstOrNull { measurable ->
                    measurable.layoutId == FriendRankingIconLayoutId
                }!!
                val nicknameMeasurable = measurables.fastFirstOrNull { measurable ->
                    measurable.layoutId == FriendNicknameLayoutId
                }!!
                val uuidMeasurable = measurables.fastFirstOrNull { measurable ->
                    measurable.layoutId == FriendUuidLayoutId
                }!!
                val scoreMeasurable = measurables.fastFirstOrNull { measurable ->
                    measurable.layoutId == FriendScoreLayoutId
                }!!

                val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

                val rankIconPlaceable = rankIconMeasurable.measure(looseConstraints)
                val nicknamePlaceable = nicknameMeasurable.measure(looseConstraints)
                val uuidPlaceable = uuidMeasurable.measure(looseConstraints)
                val scorePlaceable = scoreMeasurable.measure(looseConstraints)

                val startPadding = 18.dp.roundToPx()
                val endPadding = 16.dp.roundToPx()
                val spacedBy = 6.dp.roundToPx()

                layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                    rankIconPlaceable.place(
                        x = startPadding,
                        y = Alignment.CenterVertically.align(
                            size = rankIconPlaceable.height,
                            space = constraints.maxHeight,
                        ),
                    )
                    nicknamePlaceable.place(
                        x = startPadding + rankIconPlaceable.width + spacedBy,
                        y = Alignment.CenterVertically.align(
                            size = nicknamePlaceable.height,
                            space = constraints.maxHeight,
                        ),
                    )
                    uuidPlaceable.place(
                        x = startPadding + rankIconPlaceable.width + spacedBy + nicknamePlaceable.width + spacedBy,
                        y = Alignment.CenterVertically.align(
                            size = uuidPlaceable.height,
                            space = constraints.maxHeight,
                        ),
                    )
                    scorePlaceable.place(
                        x = constraints.maxWidth - endPadding - scorePlaceable.width,
                        y = Alignment.CenterVertically.align(
                            size = scorePlaceable.height,
                            space = constraints.maxHeight,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateExamIsEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy((14.5).dp),
    ) {
        Box(
            Modifier
                .size(width = 28.dp, height = 35.dp)
                .paint(
                    painter = painterResource(R.drawable.ic_round_pager),
                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                ),
        )
        BasicText(
            text = "아직 생성된 문제가 없어요",
            style = WeQuizTypography.R14.asRememberComposeStyle(),
        )
    }
}

private const val ExamPageNameLayoutId = "ExamPageNameLayout"
private const val ExamPageWipBadgeLayoutId = "ExamPageWipBadgeLayout"

@Composable
private fun CreateExams(
    modifier: Modifier = Modifier,
    examPagers: ImmutableList<Pair<String, Boolean>>,
    onExamPagerClick: () -> Unit = {},
) {
    val roundedCornerShape4 = remember { RoundedCornerShape(4.dp) }
    val roundedCornerShape16 = remember { RoundedCornerShape(16.dp) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(examPagers) { (name, isWIP) ->
            Layout(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(roundedCornerShape16)
                    .background(
                        color = WeQuizColor.G8.value,
                        shape = roundedCornerShape16,
                    )
                    .clickable(onClick = onExamPagerClick)
                    .padding(horizontal = 16.dp),
                content = {
                    BasicText(
                        modifier = Modifier.layoutId(ExamPageNameLayoutId),
                        text = name,
                        style = WeQuizTypography.M16.asRememberComposeStyle(),
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (isWIP) {
                        Box(
                            modifier = Modifier
                                .layoutId(ExamPageWipBadgeLayoutId)
                                .background(
                                    color = WeQuizColor.Black.value,
                                    shape = roundedCornerShape4,
                                )
                                .padding(4.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            BasicText(
                                text = "작성 중",
                                style = WeQuizTypography.M12
                                    .change(color = WeQuizColor.S1)
                                    .asRememberComposeStyle(),
                            )
                        }
                    }
                },
            ) { measurables, constraints ->
                val pageNameMeasurable = measurables.fastFirstOrNull { measurable ->
                    measurable.layoutId == ExamPageNameLayoutId
                }!!
                val pageWipBadgeMeasurable = measurables.fastFirstOrNull { measurable ->
                    measurable.layoutId == ExamPageWipBadgeLayoutId
                }

                val horizontalPadding = 16.dp.roundToPx()

                val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
                val pageWipBadgePlaceable = pageWipBadgeMeasurable?.measure(looseConstraints)

                val pageNameConstraints = constraints
                    .copy(
                        minWidth = 0,
                        minHeight = 0,
                        maxWidth = 0
                            .plus(constraints.maxWidth)
                            .minus(horizontalPadding * 2)
                            .minus(
                                pageWipBadgePlaceable?.width?.let { width ->
                                    width + 8.dp.roundToPx()
                                } ?: 0,
                            ),
                    )
                val pageNamePlaceable = pageNameMeasurable.measure(pageNameConstraints)

                layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                    pageNamePlaceable.place(
                        x = horizontalPadding,
                        y = Alignment.CenterVertically.align(
                            size = pageNamePlaceable.height,
                            space = constraints.maxHeight,
                        ),
                    )
                    pageWipBadgePlaceable?.place(
                        x = constraints.maxHeight - horizontalPadding - pageWipBadgePlaceable.width,
                        y = Alignment.CenterVertically.align(
                            size = pageWipBadgePlaceable.height,
                            space = constraints.maxHeight,
                        ),
                    )
                }
            }
        }
    }
}
