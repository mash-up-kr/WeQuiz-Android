/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:OptIn(ExperimentalFoundationApi::class)

package team.ommaya.wequiz.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import team.ommaya.wequiz.android.home.AnswerDetailData
import team.ommaya.wequiz.android.home.QuizDetail
import team.ommaya.wequiz.android.home.QuizDetailViewMode
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lazyState = rememberLazyListState()

            val snappingLayout = remember(lazyState) {
                SnapLayoutInfoProvider(
                    lazyListState = lazyState,
                    positionInLayout = { _, _, _ -> 0 },
                )
            }
            val flingBehavior = rememberSnapFlingBehavior(snapLayoutInfoProvider = snappingLayout)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyState,
                contentPadding = PaddingValues(30.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                flingBehavior = flingBehavior,
            ) {
                items(DummyAnswerDetailDataSize) { index ->
                    LaunchedEffect(Unit) {
                        snapshotFlow { lazyState.firstVisibleItemScrollOffset }.collect {
                            val offset = lazyState.calculateCurrentOffsetForIndex(index)
                            Log.d("$index offset", offset.toString())
                        }
                    }

                    DummyQuizDetail(
                        modifier = Modifier
                            /*.graphicsLayer {
                                val itemOffset = lazyState.calculateCurrentOffsetForIndex(index)
                                translationY = itemOffset * size.height
                                alpha = 1 - itemOffset
                                scaleX = 1F - itemOffset / 2
                                scaleY = 1F - itemOffset / 2
                            }*/
                            .fillMaxWidth(),
                        index = index,
                        title = "$index 시험지!",
                        answerDatas = DummyAnswerDetailDatas[index],
                    )
                }
            }
        }
    }
}

private fun LazyListState.calculateCurrentOffsetForIndex(index: Int) =
    abs((firstVisibleItemIndex - index) + firstVisibleItemScrollOffset).toFloat()


private const val DummyAnswerDetailDataSize = 10
private val DummyAnswerDetailDatas =
    List(DummyAnswerDetailDataSize) {
        List(5) { index ->
            AnswerDetailData(
                index = index,
                content = "문제 답변\n - $index",
                chosenCount = when (index) {
                    0 -> 4
                    1 -> 43
                    else -> Random.nextInt(if (index == 2) 25 else 0, 51)
                },
                totalExamineeCount = 50,
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
