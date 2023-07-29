/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:OptIn(ExperimentalFoundationApi::class)
@file:Suppress("ConstPropertyName")

package team.ommaya.wequiz.android.home.quizdetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapFlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFirstOrNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.data.client.TmpToken
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.domain.model.statistic.QuizStatistic
import team.ommaya.wequiz.android.domain.usecase.statistic.GetQuizStatisticUseCase
import team.ommaya.wequiz.android.utils.applyIf
import team.ommaya.wequiz.android.utils.asLoose
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.get
import team.ommaya.wequiz.android.utils.noRippleClickable
import team.ommaya.wequiz.android.utils.toast
import javax.inject.Inject
import kotlin.math.abs

private const val LeadingBackIconLayoutId = "LeadingBackIconLayout"
private const val TrailingShareIconLayoutId = "TrailingShareIconLayout"
private const val TrailingDeleteIconLayoutId = "TrailingDeleteIconLayout"

@AndroidEntryPoint
class QuizDetailActivity : ComponentActivity() {
    @Inject
    lateinit var getQuizStatisticUseCase: GetQuizStatisticUseCase

    private val token by lazy { intent?.getStringExtra("token") ?: TmpToken }
    private val quizId by lazy {
        (intent?.getIntExtra("quizId", Int.MIN_VALUE) ?: Int.MIN_VALUE)
            .also { value ->
                if (value == Int.MIN_VALUE) error("No quizId!")
            }
    }

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

            var quizStatistic by remember { mutableStateOf<QuizStatistic?>(null) }

            LaunchedEffect(token, quizId) {
                quizStatistic =
                    getQuizStatisticUseCase(token = token, quizId = quizId)
                        .getOrElse { exception ->
                            toast(exception.toString())
                            quizStatistic
                        }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = WeQuizColor.G9.value),
            ) {
                Layout(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Box(
                            Modifier
                                .layoutId(LeadingBackIconLayoutId)
                                .size(24.dp)
                                .fitPaint(
                                    drawableId = R.drawable.ic_round_chevron_left_24,
                                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                                )
                                .noRippleClickable(::finish),
                        )
                        Box(
                            Modifier
                                .layoutId(TrailingShareIconLayoutId)
                                .size(24.dp)
                                .fitPaint(
                                    drawableId = R.drawable.ic_round_share_24,
                                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                                )
                                .noRippleClickable { toast("준비중...") },
                        )
                        Box(
                            Modifier
                                .layoutId(TrailingDeleteIconLayoutId)
                                .size(24.dp)
                                .fitPaint(
                                    drawableId = R.drawable.ic_round_trash_24,
                                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                                )
                                .noRippleClickable { /* TODO */ },
                        )
                    },
                ) { measurables, constraints ->
                    val looseConstraints = constraints.asLoose(width = true, height = true)

                    val backIcon = measurables[LeadingBackIconLayoutId].measure(looseConstraints)
                    val share = measurables[TrailingShareIconLayoutId].measure(looseConstraints)
                    val delete = measurables[TrailingDeleteIconLayoutId].measure(looseConstraints)

                    val width = constraints.maxWidth
                    val height = share.height + (16.dp.roundToPx() * 2)

                    layout(width = width, height = height) {
                        backIcon.place(
                            x = 12.dp.roundToPx(),
                            y = Alignment.CenterVertically.align(
                                size = backIcon.height,
                                space = height,
                            ),
                        )
                        val deletePlacementX = width - 20.dp.roundToPx() - delete.width
                        delete.place(
                            x = deletePlacementX,
                            y = Alignment.CenterVertically.align(
                                size = delete.height,
                                space = height,
                            ),
                        )
                        share.place(
                            x = deletePlacementX - 28.dp.roundToPx(),
                            y = Alignment.CenterVertically.align(
                                size = share.height,
                                space = height,
                            ),
                        )
                    }
                }

                @Suppress("NAME_SHADOWING")
                val quizStatistic = quizStatistic

                if (quizStatistic != null) {
                    val questionSize = remember(quizStatistic) { quizStatistic.questions.size }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyState,
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(30.dp),
                        flingBehavior = flingBehavior,
                    ) {
                        itemsIndexed(
                            items = quizStatistic.questions,
                            key = { _, item -> item.hashCode() },
                        ) { index, question ->
                            var itemOffset by remember { mutableFloatStateOf(0f) }

                            LaunchedEffect(lazyState) {
                                with(lazyState) {
                                    snapshotFlow { firstVisibleItemScrollOffset }.collect { firstVisibleItemScrollOffset ->
                                        if (firstVisibleItemIndex == index) {
                                            val firstVisibleSize = 0
                                                .plus(layoutInfo.visibleItemsInfo.fastFirstOrNull { it.index == index }!!.size)
                                                .plus(layoutInfo.beforeContentPadding)
                                            val scrollOffset =
                                                abs(firstVisibleItemScrollOffset).toFloat()
                                            val scrollPercent =
                                                (scrollOffset / firstVisibleSize).coerceIn(0f, 1f)

                                            itemOffset = scrollPercent
                                        }
                                    }
                                }
                            }

                            QuizDetail(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .applyIf(index != questionSize - 1) {
                                        graphicsLayer {
                                            val polishItemOffset = itemOffset * 0.2f
                                            translationY = itemOffset * size.height
                                            scaleX = 1 - polishItemOffset
                                            scaleY = 1 - polishItemOffset
                                        }
                                    },
                                index = index,
                                title = question.questionTitle,
                                answerDatas = remember(question.options) {
                                    question
                                        .options
                                        .mapIndexed { index, option ->
                                            AnswerDetailData(
                                                index = index,
                                                content = option.content,
                                                selectivity = option.selectivity,
                                            )
                                        }
                                        .toImmutableList()
                                },
                            )
                        }
                    }
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

@Composable
private fun QuizDetail(
    modifier: Modifier = Modifier,
    index: Int,
    totalQuizIndex: Int,
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
        totalQuizIndex = totalQuizIndex,
        viewMode = viewMode,
        onViewModeToggleClick = { viewMode = !viewMode },
    )
}
