/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import kotlinx.collections.immutable.PersistentList
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.utils.asLoose
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.get
import team.ommaya.wequiz.android.utils.noRippleClickable

typealias ExamNameAndIsWritingPair = Pair<String, Boolean>

private const val ExamListDeleteIconLayoutId = "ExamListDeleteIconLayout"
private const val ExamListDeleteIconContainerLayoutId = "ExamListDeleteIconContainerLayout"
private const val ExamListContentBackgroundLayoutId = "ExamListContentBackgroundLayout"
private const val ExamListNameLayoutId = "ExamListNameLayout"
private const val ExamListWipBadgeLayoutId = "ExamListWipBadgeLayout"

private val ExamListDeleteIconSize = 20.dp

private const val ExamListAninmationMillis = 300

@Composable
fun ExamList(
    modifier: Modifier = Modifier,
    exams: PersistentList<ExamNameAndIsWritingPair>,
    deleteModeEnable: Boolean = false,
    onDeleteIconClick: (index: Int) -> Unit = {},
    onExamListrClick: () -> Unit = {},
) {
    val density = LocalDensity.current
    val roundedCornerShape4 = remember { RoundedCornerShape(4.dp) }
    val roundedCornerShape16 = remember { RoundedCornerShape(16.dp) }

    val deleteModeContentWidthStatic = remember(density) {
        with(density) {
            val deleteIconAndContentGap = 12.dp.roundToPx()
            ExamListDeleteIconSize.roundToPx() + deleteIconAndContentGap
        }
    }

    val deleteIconAlpahAnimation by animateFloatAsState(
        targetValue = if (deleteModeEnable) 1f else 0f,
        animationSpec = tween(ExamListAninmationMillis),
    )
    val deleteModeContentWidthAnimation by animateIntAsState(
        targetValue = if (deleteModeEnable) deleteModeContentWidthStatic else 0,
        animationSpec = tween(ExamListAninmationMillis),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(exams) { index, (name, wip) ->
            @Suppress("RememberReturnType")
            remember(name) {
                check(name.length in 1..38) { "문제지명 길이가 1..38 이여야 합니다." }
            }

            Layout(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(tween(ExamListAninmationMillis)),
                content = {
                    Box(
                        Modifier
                            .layoutId(ExamListDeleteIconContainerLayoutId)
                            .noRippleClickable { onDeleteIconClick(index) },
                    )
                    Box(
                        Modifier
                            .layoutId(ExamListDeleteIconLayoutId)
                            .size(ExamListDeleteIconSize)
                            .fitPaint(
                                drawableId = R.drawable.ic_fill_minus_circle_24,
                                colorFilter = remember(deleteIconAlpahAnimation) {
                                    WeQuizColor.Alert
                                        .change(alpha = deleteIconAlpahAnimation)
                                        .toColorFilterOrNull()
                                },
                            ),
                    )
                    Box(
                        Modifier
                            .layoutId(ExamListContentBackgroundLayoutId)
                            .clip(roundedCornerShape16)
                            .background(
                                color = WeQuizColor.G8.value,
                                shape = roundedCornerShape16,
                            )
                            .clickable(onClick = onExamListrClick),
                    )
                    BasicText(
                        modifier = Modifier.layoutId(ExamListNameLayoutId),
                        text = name,
                        style = WeQuizTypography.M16
                            .change(color = WeQuizColor.G2)
                            .asRememberComposeStyle(),
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (wip) {
                        Box(
                            modifier = Modifier
                                .layoutId(ExamListWipBadgeLayoutId)
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
                val deleteIconMeasurable = measurables[ExamListDeleteIconLayoutId]
                val deleteIconContainerMeasurable = measurables[ExamListDeleteIconContainerLayoutId]
                val contentBackgroundMeasurable = measurables[ExamListContentBackgroundLayoutId]
                val pageNameMeasurable = measurables[ExamListNameLayoutId]
                val pageWipBadgeMeasurable = measurables.fastFirstOrNull { measurable ->
                    measurable.layoutId == ExamListWipBadgeLayoutId
                }

                val contentPadding = 16.dp.roundToPx()
                val nameAndWipBadgeGap = 8.dp.roundToPx()

                val width = constraints.maxWidth
                val contentWidth = width - deleteModeContentWidthAnimation

                val looseConstraints = constraints.asLoose(width = true, height = true)

                val pageWipBadgePlaceable = pageWipBadgeMeasurable?.measure(looseConstraints)

                val pageNameConstraints =
                    constraints.copy(
                        minWidth = 0,
                        minHeight = 0,
                        maxWidth = 0
                            .plus(contentWidth)
                            .minus(contentPadding * 2)
                            .minus(
                                pageWipBadgePlaceable?.width?.let { nameWidth ->
                                    nameWidth + nameAndWipBadgeGap
                                } ?: 0,
                            ),
                    )
                val pageNamePlaceable = pageNameMeasurable.measure(pageNameConstraints)

                val height = pageNamePlaceable.height + contentPadding * 2

                val contentConstraints = Constraints.fixed(width = contentWidth, height = height)

                val contentBackgroundPlaceable =
                    contentBackgroundMeasurable.measure(contentConstraints)

                val deleteIconPlaceable = deleteIconMeasurable.measure(looseConstraints)

                val deleteIconContainerConstraints =
                    Constraints.fixed(
                        width = deleteModeContentWidthStatic,
                        height = height,
                    )
                val deleteIconContainerPlaceable =
                    deleteIconContainerMeasurable.measure(deleteIconContainerConstraints)

                layout(width = width, height = height) {
                    if (deleteModeEnable) {
                        deleteIconContainerPlaceable.place(
                            x = 0,
                            y = 0,
                            zIndex = 1f,
                        )
                    }
                    deleteIconPlaceable.place(
                        x = 0,
                        y = Alignment.CenterVertically.align(
                            size = deleteIconPlaceable.height,
                            space = height,
                        ),
                        zIndex = 0f,
                    )
                    contentBackgroundPlaceable.place(
                        x = deleteModeContentWidthAnimation,
                        y = 0,
                        zIndex = 3f,
                    )
                    pageNamePlaceable.place(
                        x = contentPadding + deleteModeContentWidthAnimation,
                        y = Alignment.CenterVertically.align(
                            size = pageNamePlaceable.height,
                            space = height,
                        ),
                        zIndex = 4f,
                    )
                    pageWipBadgePlaceable?.place(
                        x = width - contentPadding - pageWipBadgePlaceable.width,
                        y = Alignment.CenterVertically.align(
                            size = pageWipBadgePlaceable.height,
                            space = height,
                        ),
                        zIndex = 4f,
                    )
                }
            }
        }
    }
}
