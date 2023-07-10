/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:OptIn(ExperimentalTextApi::class)

package team.ommaya.wequiz.android.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.utils.applyIf
import team.ommaya.wequiz.android.utils.asLoose
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.get

@Immutable
enum class SwipeDirection {
    None, Top, Down,
}

@Immutable
enum class QuizDetailViewMode {
    Answer, Result;

    operator fun not() = if (this == Answer) Result else Answer
}

@Immutable
data class AnswerDetailData(
    val index: Int,
    val content: String,
    val chosenCount: Int,
    val totalExamineeCount: Int,
) {
    init {
        require(index in 0..4) { "답변의 index($index)는 0..4 여야 합니다." }
    }

    @Stable
    val backgroundColor =
        when (index) {
            0 -> WeQuizColor.S1
            1 -> WeQuizColor.S2
            2 -> WeQuizColor.S3
            3 -> WeQuizColor.S4
            4 -> WeQuizColor.S5
            else -> throw IndexOutOfBoundsException()
        }

    @Stable
    val chosenPercent = 100 * chosenCount / totalExamineeCount

    // TODO: 블렌딩 문제 수정 (https://youtube.com/watch?v=v4E2Jp4lXt8)
    @Stable
    fun overlayColorForBackgroundColorSafely(chosenPercentThreshold: Int) =
        if (chosenPercentThreshold <= chosenPercent) {
            if (backgroundColor.value.luminance() <= 0.5) WeQuizColor.G1
            else WeQuizColor.G9
        } else {
            WeQuizColor.G1
        }
}

private val QuizDetailContainerShape = RoundedCornerShape(16.dp)

private const val QuizDetailAnimationDurationMillis = 300
private val QuizDetailFlipTween =
    tween<Float>(
        durationMillis = QuizDetailAnimationDurationMillis,
        easing = FastOutSlowInEasing,
    )

@Immutable
@JvmInline
value class AnswerModeHeightStoreKey private constructor(private val key: Int) {
    companion object {
        fun of(index: Int, title: String) =
            AnswerModeHeightStoreKey(index.hashCode() + title.hashCode() * 31)
    }
}

@Composable
fun QuizDetail(
    modifier: Modifier = Modifier,
    title: String,
    answerDatas: ImmutableList<AnswerDetailData>,
    answerModeHeightStore: PersistentMap<AnswerModeHeightStoreKey, Int>,
    quizIndex: Int,
    totalQuizIndex: Int,
    viewMode: QuizDetailViewMode,
    onViewModeToggleClick: () -> Unit,
    updateAnswerModeHeightStore: (newAnswerModeHeightStore: PersistentMap<AnswerModeHeightStoreKey, Int>) -> Unit,
) {
    val answerContents = remember(answerDatas) {
        answerDatas.fastMap(AnswerDetailData::content).toImmutableList()
    }
    val rotation by animateFloatAsState(
        targetValue = when (viewMode) {
            QuizDetailViewMode.Answer -> 0f
            QuizDetailViewMode.Result -> 180f
        },
        animationSpec = QuizDetailFlipTween,
    )
    val answerModeHeightScoreKey = remember(quizIndex, title) {
        AnswerModeHeightStoreKey.of(index = quizIndex, title = title)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
    ) {
        if (rotation <= 90f) {
            QuizAnswer(
                modifier = Modifier
                    .then(Modifier)
                    .applyIf(answerModeHeightStore[answerModeHeightScoreKey] == null) {
                        onPlaced { coordinates ->
                            val newAnswerModeHeightStore =
                                answerModeHeightStore.put(
                                    answerModeHeightScoreKey,
                                    coordinates.size.height,
                                )
                            updateAnswerModeHeightStore(newAnswerModeHeightStore)
                        }
                    },
                title = title,
                answerContents = answerContents,
                quizIndex = quizIndex,
                totalQuizIndex = totalQuizIndex,
                onViewModeToggleClick = onViewModeToggleClick,
            )
        } else {
            QuizResult(
                modifier = Modifier
                    .height(
                        with(LocalDensity.current) {
                            answerModeHeightStore[answerModeHeightScoreKey]!!.toDp()
                        },
                    )
                    .graphicsLayer { rotationY = 180f },
                title = title,
                answerDatas = answerDatas,
                quizIndex = quizIndex,
                totalQuizIndex = totalQuizIndex,
                onViewModeToggleClick = onViewModeToggleClick,
            )
        }
    }
}

@Composable
private fun QuizTitle(
    modifier: Modifier = Modifier,
    title: String,
    index: Int,
) {
    val typography = WeQuizTypography.M18.change(color = WeQuizColor.G2).asRememberComposeStyle()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        BasicText(
            text = "$index.",
            style = typography,
        )
        BasicText(
            text = title,
            style = typography,
        )
    }
}

@Composable
private fun QuizAnswerViewModeToggle(
    modifier: Modifier = Modifier,
    currentQuizIndex: Int,
    quizIndex: Int,
    viewMode: QuizDetailViewMode,
    onViewModeToggleClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = "$currentQuizIndex/$quizIndex",
            style = WeQuizTypography.M14
                .change(color = WeQuizColor.G2)
                .asRememberComposeStyle(),
        )
        Row(
            modifier = Modifier
                .clickable(onClick = onViewModeToggleClick)
                .padding(
                    start = 12.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 4.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicText(
                text = when (viewMode) {
                    QuizDetailViewMode.Answer -> "눌러서 결과보기"
                    QuizDetailViewMode.Result -> "눌러서 답변보기"
                },
                style = WeQuizTypography.M14
                    .change(color = WeQuizColor.G2)
                    .asRememberComposeStyle(),
            )
            Box(
                Modifier
                    .size(24.dp)
                    .fitPaint(
                        drawableId = R.drawable.ic_round_chevron_right_24,
                        colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                    ),
            )
        }
    }
}

@Composable
private fun QuizAnswer(
    modifier: Modifier = Modifier,
    title: String,
    answerContents: ImmutableList<String>,
    quizIndex: Int,
    totalQuizIndex: Int,
    onViewModeToggleClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(QuizDetailContainerShape)
            .background(color = WeQuizColor.G8.value, shape = QuizDetailContainerShape)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp,
                bottom = 22.dp,
            ),
    ) {
        QuizTitle(title = title, index = quizIndex)
        if (answerContents.isNotEmpty()) {
            Spacer(Modifier.height((28 - 12).dp))
        }
        answerContents.fastForEach { answerContent ->
            QuizAnswerTitle(
                modifier = Modifier.padding(top = 12.dp),
                title = answerContent,
            )
        }
        QuizAnswerViewModeToggle(
            modifier = Modifier.padding(top = 40.dp, start = 12.dp),
            currentQuizIndex = quizIndex,
            quizIndex = totalQuizIndex,
            viewMode = QuizDetailViewMode.Answer,
            onViewModeToggleClick = onViewModeToggleClick,
        )
    }
}

@Composable
private fun QuizResult(
    modifier: Modifier = Modifier,
    title: String,
    answerDatas: ImmutableList<AnswerDetailData>,
    quizIndex: Int,
    totalQuizIndex: Int,
    onViewModeToggleClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(QuizDetailContainerShape)
            .background(color = WeQuizColor.G8.value, shape = QuizDetailContainerShape)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp,
                bottom = 22.dp,
            ),
    ) {
        QuizTitle(title = title, index = quizIndex)
        if (answerDatas.isNotEmpty()) {
            Spacer(Modifier.height((28 - 12).dp))
        }
        answerDatas.fastForEach { answerData ->
            QuizAnswerResult(
                modifier = Modifier.padding(top = 12.dp),
                answerData = answerData,
            )
        }
        Spacer(Modifier.weight(1f))
        QuizAnswerViewModeToggle(
            modifier = Modifier.padding(top = 40.dp, start = 12.dp),
            currentQuizIndex = quizIndex,
            quizIndex = totalQuizIndex,
            viewMode = QuizDetailViewMode.Result,
            onViewModeToggleClick = onViewModeToggleClick,
        )
    }
}

@Composable
private fun ACircle(
    modifier: Modifier = Modifier,
    color: WeQuizColor,
) {
    val textMeasurer = rememberTextMeasurer()
    val typography = WeQuizTypography.M14.change(color = color).asRememberComposeStyle()

    Box(
        modifier
            .size(24.dp)
            .border(
                width = (1.5).dp,
                color = color.value,
                shape = CircleShape,
            )
            .drawWithCache {
                val textMeasureResult = textMeasurer.measure(text = "A", style = typography)
                val textPlacementTopLeft =
                    Offset(
                        x = Alignment
                            .CenterHorizontally
                            .align(
                                size = textMeasureResult.size.width,
                                space = size.width.toInt(),
                                layoutDirection = layoutDirection,
                            )
                            .toFloat(),
                        y = Alignment
                            .CenterVertically
                            .align(
                                size = textMeasureResult.size.height,
                                space = size.height.toInt(),
                            )
                            .toFloat(),
                    )

                onDrawWithContent {
                    drawText(
                        textLayoutResult = textMeasureResult,
                        topLeft = textPlacementTopLeft,
                    )
                }
            },
    )
}

private val QuizDetailContentShape = RoundedCornerShape(16.dp)

@Composable
private fun QuizAnswerTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(QuizDetailContentShape)
            .background(color = WeQuizColor.G9.value)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ACircle(color = WeQuizColor.G2)
        BasicText(
            text = title,
            style = WeQuizTypography.M16
                .change(color = WeQuizColor.G2)
                .asRememberComposeStyle(),
        )
    }
}

private const val QuizAnswerResultHeaderLayoutId = "QuizAnswerResultHeaderLayout"
private const val QuizAnswerResultPercentLayoutId = "QuizAnswerResultPercentLayout"
private const val QuizAnswerResultBackgroundLayoutId = "QuizAnswerResultBackgroundLayout"

private val QuizAnwserResultBackgroundOffsetTween =
    tween<Int>(
        durationMillis = QuizDetailAnimationDurationMillis,
        easing = LinearEasing,
    )

@Composable
private fun QuizAnswerResult(
    modifier: Modifier = Modifier,
    answerData: AnswerDetailData,
) {
    val coroutineScope = rememberCoroutineScope()
    val backgroundWidthAnimatable = remember {
        Animatable(
            initialValue = 0,
            typeConverter = Int.VectorConverter,
        )
    }

    Layout(
        modifier = modifier
            .fillMaxWidth()
            .clip(QuizDetailContentShape)
            .background(color = WeQuizColor.G9.value),
        content = {
            ACircle(
                modifier = Modifier.layoutId(QuizAnswerResultHeaderLayoutId),
                color = answerData.overlayColorForBackgroundColorSafely(chosenPercentThreshold = 5),
            )
            BasicText(
                modifier = Modifier.layoutId(QuizAnswerResultPercentLayoutId),
                text = "${answerData.chosenPercent}%",
                style = WeQuizTypography.M16
                    .change(
                        color = answerData.overlayColorForBackgroundColorSafely(
                            chosenPercentThreshold = 80,
                        ),
                    )
                    .asRememberComposeStyle(),
            )
            Box(
                modifier = Modifier
                    .layoutId(QuizAnswerResultBackgroundLayoutId)
                    .background(color = answerData.backgroundColor.value),
            )
        },
    ) { measurables, constraints ->
        val headerMeasurable = measurables[QuizAnswerResultHeaderLayoutId]
        val percentMeasurable = measurables[QuizAnswerResultPercentLayoutId]
        val backgroundMeasurable = measurables[QuizAnswerResultBackgroundLayoutId]

        val spacedByPx = 16.dp.roundToPx()
        val looseConstraints = constraints.asLoose(width = true, height = true)

        val headerPlaceable = headerMeasurable.measure(looseConstraints)
        val percentPlaceable = percentMeasurable.measure(looseConstraints)

        val width = constraints.maxWidth
        val height = headerPlaceable.height + spacedByPx * 2

        val backgroundConstraints =
            Constraints.fixed(
                width = backgroundWidthAnimatable.value,
                height = height,
            )
        val backgroundPlaceable = backgroundMeasurable.measure(backgroundConstraints)

        if (backgroundWidthAnimatable.value == 0) {
            coroutineScope.launch {
                backgroundWidthAnimatable
                    .animateTo(
                        targetValue = width * answerData.chosenCount / answerData.totalExamineeCount,
                        animationSpec = QuizAnwserResultBackgroundOffsetTween,
                    )
            }
        }

        layout(width = width, height = height) {
            headerPlaceable.place(
                x = spacedByPx,
                y = Alignment
                    .CenterVertically
                    .align(
                        size = headerPlaceable.height,
                        space = height,
                    ),
                zIndex = 1f,
            )
            percentPlaceable.place(
                x = width - spacedByPx - percentPlaceable.width,
                y = Alignment
                    .CenterVertically
                    .align(
                        size = percentPlaceable.height,
                        space = height,
                    ),
                zIndex = 1f,
            )
            backgroundPlaceable.place(
                x = 0,
                y = 0,
                zIndex = 0f,
            )
        }
    }
}
