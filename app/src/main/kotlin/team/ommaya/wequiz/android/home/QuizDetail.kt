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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.utils.asLoose
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.get
import team.ommaya.wequiz.android.utils.noRippleClickable
import kotlin.math.roundToInt

@Immutable
enum class QuizDetailViewMode {
    Answer, Result;

    operator fun not() = if (this == Answer) Result else Answer

    companion object {
        val Saver = Saver<QuizDetailViewMode, Int>(
            save = { viewMode -> viewMode.ordinal },
            restore = { ordinal -> QuizDetailViewMode.values()[ordinal] },
        )
    }
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

    @Stable
    val overlayColorForBackgroundColor =
        if (backgroundColor.value.luminance() <= 0.5) WeQuizColor.G1
        else WeQuizColor.G9
}

private val QuizDetailContainerShape = RoundedCornerShape(16.dp)

private const val QuizDetailAnimationDurationMillis = 300
private val QuizDetailFlipTween =
    tween<Float>(
        durationMillis = QuizDetailAnimationDurationMillis,
        easing = FastOutSlowInEasing,
    )

@Composable
fun QuizDetail(
    modifier: Modifier = Modifier,
    title: String,
    answerDatas: ImmutableList<AnswerDetailData>,
    quizIndex: Int,
    totalQuizIndex: Int,
    viewMode: QuizDetailViewMode,
    onViewModeToggleClick: () -> Unit,
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

    var lastAnswerModeHeight by rememberSaveable { mutableStateOf<Int?>(null) }

    Box(
        modifier = modifier.graphicsLayer {
            rotationY = rotation
            cameraDistance = 12f * density
        },
    ) {
        if (rotation <= 90f) {
            QuizAnswer(
                modifier = Modifier.layout { measurable, constraints ->
                    val looseConstraints = constraints.asLoose(width = true, height = true)
                    val placeable = measurable.measure(looseConstraints)

                    lastAnswerModeHeight = placeable.height
                    layout(width = placeable.width, height = placeable.height) {
                        placeable.place(x = 0, y = 0)
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
                    .height(with(LocalDensity.current) { lastAnswerModeHeight!!.toDp() })
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
        BasicText(text = "$index.", style = typography)
        BasicText(text = title, style = typography)
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
                .noRippleClickable(onClick = onViewModeToggleClick)
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
        answerContents.fastForEachIndexed { answerIndex, answerContent ->
            QuizAnswerTitle(
                modifier = Modifier.padding(top = 12.dp),
                title = answerContent,
                index = answerIndex,
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

private enum class DrawTime {
    Behind, Front,
}

private val HeaderChars = charArrayOf('A', 'B', 'C', 'D', 'E')

private fun CacheDrawScope.withDrawHeaderCharCircle(
    index: Int,
    textMeasurer: TextMeasurer,
    color: WeQuizColor,
    center: Offset,
    topLeft: Density.(textMeasureResult: TextLayoutResult) -> Offset,
    drawTime: DrawTime = DrawTime.Front,
    onDrawBehind: (ContentDrawScope.() -> Unit)? = null,
    onDrawFront: (ContentDrawScope.() -> Unit)? = null,
): DrawResult {
    val typography = WeQuizTypography.M14.change(color = color).asComposeStyle()
    val textMeasureResult =
        textMeasurer.measure(text = HeaderChars[index].toString(), style = typography)

    val circleStyle = Stroke(width = (1.5).dp.toPx(), cap = StrokeCap.Round)
    val topLeftResult = topLeft(textMeasureResult)

    val drawingBlock: ContentDrawScope.() -> Unit = {
        drawCircle(
            color = color.value,
            radius = 12.dp.toPx(),
            center = center,
            style = circleStyle,
        )
        drawText(
            textLayoutResult = textMeasureResult,
            topLeft = topLeftResult,
        )
    }

    return onDrawWithContent {
        onDrawBehind?.invoke(this)
        if (drawTime == DrawTime.Behind) drawingBlock()
        drawContent()
        onDrawFront?.invoke(this)
        if (drawTime == DrawTime.Front) drawingBlock()
    }
}

private val QuizDetailContentShape = RoundedCornerShape(16.dp)

@Composable
private fun QuizAnswerTitle(
    modifier: Modifier = Modifier,
    title: String,
    index: Int,
) {
    val textMeasurer = rememberTextMeasurer(cacheSize = 1)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(QuizDetailContentShape)
            .background(color = WeQuizColor.G9.value)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(24.dp)
                .drawWithCache {
                    withDrawHeaderCharCircle(
                        index = index,
                        textMeasurer = textMeasurer,
                        color = WeQuizColor.G2,
                        center = size.center,
                        topLeft = { textMeasureResult ->
                            Offset(
                                x = Alignment
                                    .CenterHorizontally
                                    .align(
                                        size = textMeasureResult.size.width,
                                        space = size.width.roundToInt(),
                                        layoutDirection = layoutDirection,
                                    )
                                    .toFloat(),
                                y = Alignment
                                    .CenterVertically
                                    .align(
                                        size = textMeasureResult.size.height,
                                        space = size.height.roundToInt(),
                                    )
                                    .toFloat(),
                            )
                        },
                    )
                }
        )
        BasicText(
            text = title,
            style = WeQuizTypography.M16
                .change(color = WeQuizColor.G2)
                .asRememberComposeStyle(),
        )
    }
}

private const val QuizAnswerResultBackgroundLayoutId = "QuizAnswerResultBackgroundLayout"

private val QuizAnwserResultBackgroundOffsetTween =
    tween<Int>(
        durationMillis = QuizDetailAnimationDurationMillis,
        easing = LinearEasing,
    )

@Stable
private class LazyValue<T>(var value: T? = null)

@Composable
private fun QuizAnswerResult(
    modifier: Modifier = Modifier,
    answerData: AnswerDetailData,
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val textMeasurer = rememberTextMeasurer()
    val recomposeScope = currentRecomposeScope

    val spacedBy = 16.dp
    val spacedByPx = remember(density, spacedBy) {
        with(density) { spacedBy.roundToPx() }
    }
    val lazyParentWidth = remember { LazyValue<Int>() }

    val headCharCircleSize = 24.dp
    val backgroundWidthAnimatable = remember {
        Animatable(
            initialValue = 0,
            typeConverter = Int.VectorConverter,
        )
    }

    val headerCharCircleCenterOffsetGetter =
        remember<CacheDrawScope.() -> Offset>(spacedBy, headCharCircleSize) {
            {
                Offset(
                    x = spacedBy.toPx() + (headCharCircleSize.toPx() / 2),
                    y = size.height / 2,
                )
            }
        }
    val headerCharCircleTopLeftOffsetGetter =
        remember<CacheDrawScope.(textMeasureResult: TextLayoutResult) -> Offset>(
            spacedBy,
            headCharCircleSize,
        ) {
            { textMeasureResult ->
                Offset(
                    x = Alignment
                        .CenterHorizontally
                        .align(
                            size = textMeasureResult.size.width,
                            space = headCharCircleSize.roundToPx(),
                            layoutDirection = layoutDirection,
                        )
                        .plus(spacedBy.toPx()),
                    y = Alignment
                        .CenterVertically
                        .align(
                            size = textMeasureResult.size.height,
                            space = size.height.roundToInt(),
                        )
                        .toFloat(),
                )
            }
        }

    val typography = WeQuizTypography.M16.asRememberComposeStyle()
    val percentText = "${answerData.chosenPercent}%"

    val percentInfinityMeasureResult = remember(textMeasurer, answerData) {
        textMeasurer.measure(text = percentText, style = typography)
    }

    Layout(
        modifier = modifier
            .fillMaxWidth()
            .clip(QuizDetailContentShape)
            .background(color = WeQuizColor.G9.value)
            .drawWithCache {
                val headerCharCircleCenterOffset = headerCharCircleCenterOffsetGetter()

                withDrawHeaderCharCircle(
                    index = answerData.index,
                    textMeasurer = textMeasurer,
                    color = WeQuizColor.G1,
                    center = headerCharCircleCenterOffset,
                    topLeft = { headerCharCircleTopLeftOffsetGetter(it) },
                    drawTime = DrawTime.Behind,
                    onDrawBehind = {
                        drawText(
                            textLayoutResult = percentInfinityMeasureResult,
                            color = WeQuizColor.G1.value,
                            topLeft = Offset(
                                x = size.width - spacedByPx - percentInfinityMeasureResult.size.width,
                                y = Alignment
                                    .CenterVertically
                                    .align(
                                        size = percentInfinityMeasureResult.size.height,
                                        space = size.height.roundToInt(),
                                    )
                                    .toFloat(),
                            ),
                        )
                    },
                )
            },
        content = {
            Box(
                modifier = Modifier
                    .layoutId(QuizAnswerResultBackgroundLayoutId)
                    .background(color = answerData.backgroundColor.value)
                    .drawWithCache {
                        val headerCharCircleCenterOffset = headerCharCircleCenterOffsetGetter()

                        val precentClippedMeasureResult =
                            lazyParentWidth.value?.let { parentWidth ->
                                textMeasurer.measure(
                                    text = percentText,
                                    style = typography,
                                    maxLines = 1,
                                    softWrap = false,
                                    constraints = Constraints(
                                        maxWidth = 0
                                            .plus(size.width)
                                            .minus(parentWidth - spacedByPx - percentInfinityMeasureResult.size.width)
                                            .coerceAtLeast(0f)
                                            .roundToInt(),
                                    ),
                                )
                            }

                        withDrawHeaderCharCircle(
                            index = answerData.index,
                            textMeasurer = textMeasurer,
                            color = answerData.overlayColorForBackgroundColor,
                            center = headerCharCircleCenterOffset,
                            topLeft = { headerCharCircleTopLeftOffsetGetter(it) },
                            drawTime = DrawTime.Front,
                            onDrawFront = {
                                if (precentClippedMeasureResult != null) {
                                    val parentWidth = lazyParentWidth.value!!
                                    drawText(
                                        textLayoutResult = precentClippedMeasureResult,
                                        color = answerData.overlayColorForBackgroundColor.value,
                                        topLeft = Offset(
                                            x = parentWidth.toFloat() - spacedByPx - percentInfinityMeasureResult.size.width,
                                            y = Alignment
                                                .CenterVertically
                                                .align(
                                                    size = percentInfinityMeasureResult.size.height,
                                                    space = size.height.roundToInt(),
                                                )
                                                .toFloat(),
                                        ),
                                    )
                                }
                            },
                        )
                    },
            )
        },
    ) { measurables, constraints ->
        val backgroundMeasurable = measurables[QuizAnswerResultBackgroundLayoutId]

        val width = constraints.maxWidth
        val height = headCharCircleSize.roundToPx() + spacedByPx * 2

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

        if (lazyParentWidth.value == null) {
            lazyParentWidth.value = width
            recomposeScope.invalidate()
        }

        layout(width = width, height = height) {
            backgroundPlaceable.place(x = 0, y = 0)
        }
    }
}
