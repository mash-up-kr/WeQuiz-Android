/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import kotlinx.collections.immutable.ImmutableList
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor

@Immutable
enum class QuizDetailViewMode {
    Awnser, Result,
}

@Immutable
data class AwnserDetailData(
    val index: Int,
    val content: String,
    val isCorrect: Boolean,
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
}

private val DetailContainerShape = RoundedCornerShape(30.dp)
private val DetailAwnserShape = RoundedCornerShape(16.dp)

@Composable
fun QuizDetail(
    modifier: Modifier = Modifier,
    quiz: String,
    awnsers: ImmutableList<AwnserDetailData>,
    currentQuizIndex: Int,
    totalQuizIndex: Int,
    detailViewMode: QuizDetailViewMode,
    onViewModeToggleClick: () -> Unit,
) {
    val anwserContents = remember(awnsers) {
        awnsers.fastMap(AwnserDetailData::content)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clip(DetailContainerShape)
            .background(color = WeQuizColor.G8.value)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp,
                bottom = 22.dp,
            ),
    ) {

    }
}

@Composable
private fun QuizTitle(
    modifier: Modifier = Modifier,
    title: String,
    index: Int,
) {
    Layout(
        modifier = modifier.fillMaxWidth(),
        content = {

        }
    ) {

    }
}

@Composable
private fun QuizAwnserView(
    modifier: Modifier = Modifier,
    awnsers: ImmutableList<String>,
) {

}

@Composable
private fun QuizResultView(
    modifier: Modifier = Modifier,
    awnserResults: ImmutableList<AwnserDetailData>,
) {

}
