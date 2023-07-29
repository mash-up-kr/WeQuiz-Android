/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.utils.asLoose

@Composable
fun QuizDeleteConfirmDialog(
    onDismissRequest: () -> Unit,
    deleteIndex: Int?,
    deleteAction: (index: Int) -> Unit,
) {
    val shape = remember { RoundedCornerShape(12.dp) }

    if (deleteIndex != null) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = WeQuizColor.G7.value, shape = shape)
                    .padding(horizontal = 12.dp),
            ) {
                BasicText(
                    modifier = Modifier.layout { measurable, constraints ->
                        val looseConstraints = constraints.asLoose(width = true, height = true)
                        val placeable = measurable.measure(looseConstraints)

                        val width = constraints.maxWidth
                        val height = placeable.height + 40.dp.roundToPx() * 2

                        layout(width = width, height = height) {
                            placeable.place(
                                x = Alignment
                                    .CenterHorizontally
                                    .align(
                                        size = placeable.width,
                                        space = width,
                                        layoutDirection = layoutDirection,
                                    ),
                                y = Alignment
                                    .CenterVertically
                                    .align(
                                        size = placeable.height,
                                        space = height,
                                    ),
                            )
                        }
                    },
                    text = "해당 시험지를 삭제할까요?",
                    style = WeQuizTypography.R16
                        .change(color = WeQuizColor.G2)
                        .asRememberComposeStyle(),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .clip(shape)
                            .background(color = WeQuizColor.G6.value, shape = shape)
                            .clickable(onClick = onDismissRequest)
                            .weight(1f)
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        BasicText(
                            text = "아니요",
                            style = WeQuizTypography.SB16
                                .change(color = WeQuizColor.G2)
                                .asRememberComposeStyle(),
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(shape)
                            .background(color = WeQuizColor.P1.value, shape = shape)
                            .clickable { deleteAction(deleteIndex) }
                            .weight(1f)
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        BasicText(
                            text = "삭제",
                            style = WeQuizTypography.SB16
                                .change(color = WeQuizColor.G2)
                                .asRememberComposeStyle(),
                        )
                    }
                }
            }
        }
    }
}
