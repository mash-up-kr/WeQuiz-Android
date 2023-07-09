/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.home.AnswerDetailData
import team.ommaya.wequiz.android.home.AnswerModeHeightStoreKey
import team.ommaya.wequiz.android.home.QuizDetail
import team.ommaya.wequiz.android.home.QuizDetailViewMode
import team.ommaya.wequiz.android.home.SwipeDirection
import team.ommaya.wequiz.android.utils.asLoose
import team.ommaya.wequiz.android.utils.get
import java.util.Stack
import kotlin.random.Random

@Immutable
private data class QuizDetailComposableHolder(
  val scaleAnimatable: Animatable<Float, AnimationVector1D>,
  val xOffsetAnimatable: Animatable<Int, AnimationVector1D>,
  val stackComposableHeightStoreKey: AnswerModeHeightStoreKey,
  val composable: @Composable () -> Unit,
)

private const val QuizDetailStackComposableLayoutId = "QuizDetailStackComposableLayout"
private const val QuizDetailCandidateComposableLayoutId = "QuizDetailCandidateComposableLayout"

@Stable
private fun <T> Stack<T>.popOrNull() = if (!empty()) pop() else null

@Stable
private fun <T> Stack<T>.peekOrNull() = if (!empty()) peek() else null

private const val ShrinkScale = 0.8f
private const val ExpandScale = 1.0f

private const val DefaultTweenAnimationMillis = 300
private val OffsetTween =
  tween<Int>(
    durationMillis = DefaultTweenAnimationMillis,
    easing = FastOutSlowInEasing,
  )
private val ScaleTween =
  tween<Float>(
    durationMillis = DefaultTweenAnimationMillis,
    easing = FastOutSlowInEasing,
  )

private val lazyLambdas = mutableListOf<() -> Unit>()

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      var swipeDirection by remember { mutableStateOf(SwipeDirection.None) }
      val stackComposables = remember { Stack<QuizDetailComposableHolder>() }
      val candidateComposables = remember { Stack<QuizDetailComposableHolder>() }

      val coroutineScope = rememberCoroutineScope()
      val density = LocalDensity.current

      val contentSpacedByPx = remember(density) {
        with(density) { 30.dp.roundToPx() }
      }

      Layout(
        modifier = Modifier
          .fillMaxSize()
          .pointerInput(Unit) {
            var localSwipeDirection = SwipeDirection.None
            detectVerticalDragGestures(
              onVerticalDrag = { change, dragAmount ->
                change.consume()
                when {
                  dragAmount > 0 -> localSwipeDirection = SwipeDirection.Down
                  dragAmount < 0 -> localSwipeDirection = SwipeDirection.Top
                }
              },
              onDragEnd = {
                swipeDirection = localSwipeDirection
              },
            )
          }
          .padding(
            top = 20.dp,
            start = 20.dp,
            end = 20.dp,
          ),
        content = {
          SideEffect {
            lazyLambdas.onEach { it.invoke() }.clear()

            // ensure consumed.
            swipeDirection = SwipeDirection.None
          }

          Box(
            modifier = Modifier
              .layoutId(QuizDetailStackComposableLayoutId)
              .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
            propagateMinConstraints = true,
          ) {
            stackComposables.peekOrNull()?.let { stackComposable ->
              val (scaleAnimatable, _, _, content) = stackComposable

              when (swipeDirection) {
                SwipeDirection.Top -> {
                  if (scaleAnimatable.targetValue != ShrinkScale) {
                    coroutineScope.launch {
                      scaleAnimatable
                        .animateTo(
                          targetValue = ShrinkScale,
                          animationSpec = ScaleTween,
                        )
                    }
                  }

                  if (scaleAnimatable.isRunning && scaleAnimatable.value != ShrinkScale) {
                    content()
                  }

                  lazyLambdas += {
                    candidateComposables.popOrNull()?.let { candidateComposable ->
                      stackComposables.push(candidateComposable)
                    }
                  }
                }
                SwipeDirection.Down -> {
                  if (scaleAnimatable.targetValue != ExpandScale) {
                    coroutineScope.launch {
                      scaleAnimatable.animateTo(
                        targetValue = ExpandScale,
                        animationSpec = ScaleTween,
                      )
                    }
                  }

                  content()

                  lazyLambdas += {
                    stackComposables.popOrNull()?.let { stackComposable ->
                      candidateComposables.push(stackComposable)
                    }
                  }
                }
                else -> Unit // do nothing.
              }
            }
          }
          Box(
            modifier = Modifier
              .layoutId(QuizDetailCandidateComposableLayoutId)
              .fillMaxWidth(),
            propagateMinConstraints = true,
          ) {
            candidateComposables.peekOrNull()?.let { candidateComposable ->
              val (_, xOffsetAnimatable, stackComposableHeightStoreKey, content) = candidateComposable

              when (swipeDirection) {
                SwipeDirection.Top -> {
                  if (xOffsetAnimatable.targetValue != 0) {
                    coroutineScope.launch {
                      xOffsetAnimatable.animateTo(
                        targetValue = 0,
                        animationSpec = OffsetTween,
                      )
                    }
                  }

                  content()
                }
                SwipeDirection.Down -> {
                  val candidateXOffset =
                    remember(
                      answerModeHeightStore.value,
                      stackComposableHeightStoreKey,
                      contentSpacedByPx
                    ) {
                      answerModeHeightStore.value[stackComposableHeightStoreKey]!! + contentSpacedByPx
                    }

                  if (xOffsetAnimatable.targetValue != candidateXOffset) {
                    coroutineScope.launch {
                      xOffsetAnimatable
                        .animateTo(
                          targetValue = candidateXOffset,
                          animationSpec = OffsetTween,
                        )
                    }
                  }

                  content()
                }
                else -> Unit // do nothing.
              }
            }
          }
        },
      ) { measurables, constraints ->
        val stackMeasurable = measurables[QuizDetailStackComposableLayoutId]
        val candidateMeasurable = measurables[QuizDetailCandidateComposableLayoutId]

        val looseConstraints = constraints.asLoose(width = true, height = true)

        val stackPlaceable = stackMeasurable.measure(looseConstraints)
        val candidatePlaceable = candidateMeasurable.measure(looseConstraints)

        layout(width = constraints.maxWidth, height = constraints.maxHeight) {
          stackPlaceable.place(x = 0, y = 0, zIndex = 0f)
          candidatePlaceable.place(x = 0, y = 0, zIndex = 1f)
        }
      }
    }
  }
}

private var answerModeHeightStore =
  mutableStateOf(persistentMapOf<AnswerModeHeightStoreKey, Int>())

private const val DummyAnswerDetailDataSize = 10
private val DummyAnswerDetailDatas =
  List(DummyAnswerDetailDataSize) {
    List(5) { index ->
      AnswerDetailData(
        index = index,
        content = "문제 답변\n - $index",
        chosenCount = Random.nextInt(if (index == 2) 30 else 0, 51),
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
  var viewMode by remember { mutableStateOf(QuizDetailViewMode.Answer) }

  QuizDetail(
    modifier = modifier.fillMaxSize(),
    title = title,
    answerDatas = answerDatas,
    answerModeHeightStore = answerModeHeightStore.value,
    quizIndex = index,
    totalQuizIndex = DummyAnswerDetailDataSize,
    viewMode = viewMode,
    onViewModeToggleClick = { viewMode = !viewMode },
    updateAnswerModeHeightStore = { newAnswerModeHeightStore ->
      answerModeHeightStore.value = newAnswerModeHeightStore
    },
  )
}
