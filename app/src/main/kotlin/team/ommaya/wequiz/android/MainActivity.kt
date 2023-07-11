/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:OptIn(ExperimentalFoundationApi::class)

package team.ommaya.wequiz.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.SnapFlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import team.ommaya.wequiz.android.home.AnswerDetailData
import team.ommaya.wequiz.android.home.QuizDetail
import team.ommaya.wequiz.android.home.QuizDetailViewMode
import team.ommaya.wequiz.android.utils.applyIf
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lazyState = rememberLazyListState()
            val highVelocityApproachSpec = rememberSplineBasedDecay<Float>()

            val snappingLayout =
                remember(lazyState) {
                    SnapLayoutInfoProvider(
                        lazyListState = lazyState,
                        positionInLayout = { _, _, _ -> 0 },
                    )
                }
            val flingAnimationSpec =
                remember {
                    tween<Float>(durationMillis = 100, easing = FastOutSlowInEasing)
                }
            val flingBehavior =
                rememberFullyCustomizedSnapFlingBehavior(
                    snapLayoutInfoProvider = snappingLayout,
                    lowVelocityAnimationSpec = flingAnimationSpec,
                    highVelocityAnimationSpec = highVelocityApproachSpec,
                    snapAnimationSpec = flingAnimationSpec,
                    shortSnapVelocityThreshold = 400.dp,
                )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyState,
                contentPadding = PaddingValues(30.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                flingBehavior = flingBehavior,
            ) {
                itemsIndexed(
                    items = DummyAnswerDetailDatas,
                    key = { _, item -> item.hashCode() },
                ) { index, answer ->
                    var itemOffset by remember { mutableFloatStateOf(0f) }

                    LaunchedEffect(lazyState) {
                        with(lazyState) {
                            snapshotFlow { firstVisibleItemScrollOffset }.collect { firstVisibleItemScrollOffset ->
                                if (firstVisibleItemIndex == index) {
                                    val firstVisibleSize = 0
                                        .plus(layoutInfo.visibleItemsInfo.fastFirstOrNull { it.index == index }!!.size)
                                        .plus(layoutInfo.beforeContentPadding)
                                    val scrollOffset = abs(firstVisibleItemScrollOffset).toFloat()
                                    val scrollPercent =
                                        (scrollOffset / firstVisibleSize).coerceIn(0f, 1f)

                                    itemOffset = scrollPercent
                                }
                            }
                        }
                    }

                    DummyQuizDetail(
                        modifier = Modifier
                            .fillMaxWidth()
                            .applyIf(index != DummyAnswerDetailDataSize - 1) {
                                graphicsLayer {
                                    val polishItemOffset = itemOffset * 0.2f
                                    translationY = itemOffset * size.height
                                    scaleX = 1 - polishItemOffset
                                    scaleY = 1 - polishItemOffset
                                }
                            },
                        /*.applyIf(index == DummyAnswerDetailDataSize - 1) {
                            layout { measurable, constraints ->
                                val looseConstraints =
                                    constraints.asLoose(width = true, height = true)
                                val placeable = measurable.measure(looseConstraints)

                                layout(
                                    width = placeable.width,
                                    height = constraints.maxHeight,
                                ) {
                                    placeable.place(x = 0, y = 0)
                                }
                            }
                        }*/
                        index = index,
                        title = "${index}번 시험지!",
                        answerDatas = answer,
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberFullyCustomizedSnapFlingBehavior(
    snapLayoutInfoProvider: SnapLayoutInfoProvider,
    lowVelocityAnimationSpec: AnimationSpec<Float>,
    highVelocityAnimationSpec: DecayAnimationSpec<Float>,
    snapAnimationSpec: AnimationSpec<Float>,
    shortSnapVelocityThreshold: Dp,
): SnapFlingBehavior {
    val density = LocalDensity.current
    return remember(
        density,
        snapLayoutInfoProvider,
        lowVelocityAnimationSpec,
        highVelocityAnimationSpec,
        snapAnimationSpec,
        shortSnapVelocityThreshold,
    ) {
        SnapFlingBehavior(
            density = density,
            snapLayoutInfoProvider = snapLayoutInfoProvider,
            lowVelocityAnimationSpec = lowVelocityAnimationSpec,
            highVelocityAnimationSpec = highVelocityAnimationSpec,
            snapAnimationSpec = snapAnimationSpec,
            shortSnapVelocityThreshold = shortSnapVelocityThreshold,
        )
    }
}

private const val DummyAnswerDetailDataSize = 10
private val DummyAnswerDetailDatas =
    List(DummyAnswerDetailDataSize) {
        List(5) { index ->
            AnswerDetailData(
                index = index,
                content = "문제 답변\n - $index",
                chosenCount = when (index) {
                    0 -> 5
                    1 -> 48
                    else -> Random.nextInt(if (index == 2) 25 else 0, 55)
                },
                totalExamineeCount = 54,
            )
        }.toImmutableList()
    }

@Composable
private fun DummyQuizDetail(
    modifier: Modifier = Modifier,
    index: Int,
    title: String,
    answerDatas: ImmutableList<AnswerDetailData>,
) {
    var viewMode by rememberSaveable(stateSaver = QuizDetailViewMode.Saver) {
        mutableStateOf(QuizDetailViewMode.Answer)
    }

    QuizDetail(
        modifier = modifier,
        title = title,
        answerDatas = answerDatas,
        quizIndex = index,
        totalQuizIndex = DummyAnswerDetailDataSize,
        viewMode = viewMode,
        onViewModeToggleClick = { viewMode = !viewMode },
    )
}
