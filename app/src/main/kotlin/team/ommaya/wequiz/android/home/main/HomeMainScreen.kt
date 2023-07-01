/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home.main

import androidx.annotation.VisibleForTesting
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.utils.asLoose
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.get

@Suppress("WRONG_ANNOTATION_TARGET")
@VisibleForTesting
typealias NicknameUuidScoreTriple = Triple<String, Int, Int>

@Suppress("WRONG_ANNOTATION_TARGET")
@VisibleForTesting
typealias ExamNameAndIsWritingPair = Pair<String, Boolean>

@Suppress("unused")
@Composable
fun HomeMainScreen(
    modifier: Modifier = Modifier,
    nickname: String,
    profileMessage: String,
    profileImageSrc: Any,
    friendsRanking: ImmutableList<NicknameUuidScoreTriple>,
    exams: ImmutableList<ExamNameAndIsWritingPair>,
    onExamCreateClick: () -> Unit = {},
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
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                BasicText(
                    text = nickname,
                    style = WeQuizTypography.B18
                        .change(color = WeQuizColor.G2)
                        .asRememberComposeStyle(),
                )
                if (profileMessage.isNotEmpty()) {
                    BasicText(
                        text = profileMessage,
                        style = WeQuizTypography.M14
                            .change(color = WeQuizColor.G4)
                            .asRememberComposeStyle(),
                        maxLines = 2,
                    )
                }
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
                style = WeQuizTypography.B16
                    .change(color = WeQuizColor.G1)
                    .asRememberComposeStyle(),
            )
        }
        if (friendsRanking.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                .fillMaxWidth()
                .padding(top = if (friendsRanking.isEmpty()) 20.dp else 26.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            SectionTitle(title = "내가 낸 문제지")
            if (exams.isEmpty()) {
                CreateExamIsEmpty(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                )
            } else {
                CreateExams(
                    exams = remember(exams) {
                        exams.take(4).toImmutableList()
                    },
                )
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
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BasicText(
            text = title,
            style = WeQuizTypography.B20
                .change(color = WeQuizColor.White)
                .asRememberComposeStyle(),
        )
        Box(
            Modifier
                .size(24.dp)
                .fitPaint(
                    drawableId = R.drawable.ic_round_chevron_right_24,
                    tint = WeQuizColor.G2,
                )
                .clickable(
                    onClick = onRightArrowClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ),
        )
    }
}

private const val FriendRankingIconLayoutId = "FriendRankingIconLayout"
private const val FriendNicknameLayoutId = "FriendNicknameLayout"
private const val FriendUuidLayoutId = "FriendUuidLayout"
private const val FriendScoreLayoutId = "FriendScoreLayout"

@Composable
private fun FriendsRanking(
    modifier: Modifier = Modifier,
    friendsRanking: ImmutableList<NicknameUuidScoreTriple>,
    onFriendClick: () -> Unit = {},
) {
    val roundedCornerShape4 = remember { RoundedCornerShape(4.dp) }
    val roundedCornerShape16 = remember { RoundedCornerShape(16.dp) }

    @Suppress("RememberReturnType")
    remember(friendsRanking) {
        check(friendsRanking.size <= 3) { "친구 랭킹은 최대 3위까지만 노출 가능합니다." }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(friendsRanking) { rankNumber, (nickname, uuid, scroe) ->
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
                        Modifier
                            .layoutId(FriendRankingIconLayoutId)
                            .size(24.dp)
                            .fitPaint(
                                drawableId = when (rankNumber) {
                                    0 -> R.drawable.ic_color_grade_gold_24
                                    1 -> R.drawable.ic_color_grade_silver_24
                                    2 -> R.drawable.ic_color_grade_bronze_24
                                    else -> throw IndexOutOfBoundsException("3위 이상의 친구 랭킹은 존재할 수 없습니다.")
                                },
                            ),
                    )
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
                        text = "${scroe}점",
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

@Composable
private fun CreateExamIsEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Box(
            Modifier
                .size(40.dp)
                .fitPaint(
                    drawableId = R.drawable.ic_fill_document_24,
                    tint = WeQuizColor.G2,
                ),
        )
        BasicText(
            text = "아직 생성된 문제가 없어요",
            style = WeQuizTypography.R14
                .change(color = WeQuizColor.G2)
                .asRememberComposeStyle(),
        )
    }
}

private const val ExamPageNameLayoutId = "ExamPageNameLayout"
private const val ExamPageWipBadgeLayoutId = "ExamPageWipBadgeLayout"

@Composable
private fun CreateExams(
    modifier: Modifier = Modifier,
    exams: ImmutableList<ExamNameAndIsWritingPair>,
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
        items(exams) { (name, isWIP) ->
            @Suppress("RememberReturnType")
            remember(name) {
                check(name.length in 1..38) { "문제지명 길이가 1..38 이여야 합니다." }
            }

            Layout(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(roundedCornerShape16)
                    .background(
                        color = WeQuizColor.G8.value,
                        shape = roundedCornerShape16,
                    )
                    .clickable(onClick = onExamPagerClick),
                content = {
                    BasicText(
                        modifier = Modifier.layoutId(ExamPageNameLayoutId),
                        text = name,
                        style = WeQuizTypography.M16
                            .change(color = WeQuizColor.G2)
                            .asRememberComposeStyle(),
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
                                .padding(vertical = 4.dp, horizontal = 12.dp),
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
                val pageNameMeasurable = measurables[ExamPageNameLayoutId]
                val pageWipBadgeMeasurable = measurables.fastFirstOrNull { measurable ->
                    measurable.layoutId == ExamPageWipBadgeLayoutId
                }

                val padding = 16.dp.roundToPx()
                val nameAndWipBadgeGap = 8.dp.roundToPx()

                val looseConstraints = constraints.asLoose(width = true, height = true)
                val pageWipBadgePlaceable = pageWipBadgeMeasurable?.measure(looseConstraints)

                val pageNameConstraints = constraints
                    .copy(
                        minWidth = 0,
                        minHeight = 0,
                        maxWidth = 0
                            .plus(constraints.maxWidth)
                            .minus(padding * 2)
                            .minus(
                                pageWipBadgePlaceable?.width?.let { nameWidth ->
                                    nameWidth + nameAndWipBadgeGap
                                } ?: 0,
                            ),
                    )
                val pageNamePlaceable = pageNameMeasurable.measure(pageNameConstraints)

                val height = pageNamePlaceable.height + padding * 2

                layout(width = constraints.maxWidth, height = height) {
                    pageNamePlaceable.place(
                        x = padding,
                        y = Alignment.CenterVertically.align(
                            size = pageNamePlaceable.height,
                            space = height,
                        ),
                    )
                    pageWipBadgePlaceable?.place(
                        x = constraints.maxWidth - padding - pageWipBadgePlaceable.width,
                        y = Alignment.CenterVertically.align(
                            size = pageWipBadgePlaceable.height,
                            space = height,
                        ),
                    )
                }
            }
        }
    }
}
