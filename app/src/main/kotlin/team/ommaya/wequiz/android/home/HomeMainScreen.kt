/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.noRippleClickable

@Suppress("unused")
@Composable
fun HomeMainScreen(
    modifier: Modifier = Modifier,
    nickname: String,
    profileMessage: String,
    profileImageSrc: Any,
    friendsRanking: ImmutableList<NicknameUuidScoreTriple>,
    quizs: ImmutableList<QuizNameAndIsWritingPair>,
    onQuizCreateClick: () -> Unit = {},
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
                .clickable(onClick = onQuizCreateClick)
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
            if (quizs.isEmpty()) {
                CreateQuizIsEmpty(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                )
            } else {
                QuizList(
                    quizs = remember(quizs) {
                        quizs.take(4).toImmutableList()
                    }.toPersistentList(),
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
                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                )
                .noRippleClickable(onRightArrowClick),
        )
    }
}

@Composable
private fun CreateQuizIsEmpty(modifier: Modifier = Modifier) {
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
                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
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
