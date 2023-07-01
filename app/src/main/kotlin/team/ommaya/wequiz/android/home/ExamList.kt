/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home

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

private const val ExamPageDeleteIconLayoutId = "ExamPageDeleteIconLayout"
private const val ExamPageDeleteIconContainerLayoutId = "ExamPageDeleteIconContainerLayout"
private const val ExamPageContentBackgroundLayoutId = "ExamPageContentBackgroundLayout"
private const val ExamPageNameLayoutId = "ExamPageNameLayout"
private const val ExamPageWipBadgeLayoutId = "ExamPageWipBadgeLayout"

private val ExamPageDeleteIconSize = 20.dp

private const val ExamPageAninmationMillis = 300

@Composable
fun ExamList(
    modifier: Modifier = Modifier,
    exams: PersistentList<ExamNameAndIsWritingPair>,
    deleteModeEnable: Boolean = false,
    onDeleteIconClick: (index: Int) -> Unit = {},
    onExamPagerClick: () -> Unit = {},
) {
    val density = LocalDensity.current
    val roundedCornerShape4 = remember { RoundedCornerShape(4.dp) }
    val roundedCornerShape16 = remember { RoundedCornerShape(16.dp) }

    val deleteModeContentWidthStatic = remember(density) {
        with(density) {
            val deleteIconAndContentGap = 12.dp.roundToPx()
            ExamPageDeleteIconSize.roundToPx() + deleteIconAndContentGap
        }
    }

    val deleteIconAlpahAnimation by animateFloatAsState(
        targetValue = if (deleteModeEnable) 1f else 0f,
        animationSpec = tween(ExamPageAninmationMillis),
    )
    val deleteModeContentWidthAnimation by animateIntAsState(
        targetValue = if (deleteModeEnable) deleteModeContentWidthStatic else 0,
        animationSpec = tween(ExamPageAninmationMillis),
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
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Box(
                        Modifier
                            .layoutId(ExamPageDeleteIconContainerLayoutId)
                            .noRippleClickable { onDeleteIconClick(index) },
                    )
                    Box(
                        Modifier
                            .layoutId(ExamPageDeleteIconLayoutId)
                            .size(ExamPageDeleteIconSize)
                            .fitPaint(
                                drawableId = R.drawable.ic_fill_minus_circle_24,
                                tint = WeQuizColor.Alert.change(deleteIconAlpahAnimation),
                                keys = arrayOf(deleteIconAlpahAnimation),
                            ),
                    )
                    Box(
                        Modifier
                            .layoutId(ExamPageContentBackgroundLayoutId)
                            .clip(roundedCornerShape16)
                            .background(
                                color = WeQuizColor.G8.value,
                                shape = roundedCornerShape16,
                            )
                            .clickable(onClick = onExamPagerClick),
                    )
                    BasicText(
                        modifier = Modifier.layoutId(ExamPageNameLayoutId),
                        text = name,
                        style = WeQuizTypography.M16
                            .change(color = WeQuizColor.G2)
                            .asRememberComposeStyle(),
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (wip) {
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
                val deleteIconMeasurable = measurables[ExamPageDeleteIconLayoutId]
                val deleteIconContainerMeasurable = measurables[ExamPageDeleteIconContainerLayoutId]
                val contentBackgroundMeasurable = measurables[ExamPageContentBackgroundLayoutId]
                val pageNameMeasurable = measurables[ExamPageNameLayoutId]
                val pageWipBadgeMeasurable = measurables.fastFirstOrNull { measurable ->
                    measurable.layoutId == ExamPageWipBadgeLayoutId
                }

                val contentPadding = 16.dp.roundToPx()
                val nameAndWipBadgeGap = 8.dp.roundToPx()

                val contentWidth = constraints.maxWidth - deleteModeContentWidthAnimation

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

                layout(width = constraints.maxWidth, height = height) {
                    deleteIconContainerPlaceable.place(
                        x = 0,
                        y = 0,
                        zIndex = 1f,
                    )
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
                        x = constraints.maxWidth - contentPadding - pageWipBadgePlaceable.width,
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
