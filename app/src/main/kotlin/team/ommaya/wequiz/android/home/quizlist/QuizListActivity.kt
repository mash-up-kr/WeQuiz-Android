/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

@file:Suppress("ConstPropertyName")

package team.ommaya.wequiz.android.home.quizlist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.data.client.TmpToken
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.domain.model.quiz.Quiz
import team.ommaya.wequiz.android.domain.usecase.quiz.DeleteQuizUseCase
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizListUseCase
import team.ommaya.wequiz.android.home.quizdetail.QuizDetailActivity
import team.ommaya.wequiz.android.utils.asLoose
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.get
import team.ommaya.wequiz.android.utils.noRippleClickable
import team.ommaya.wequiz.android.utils.toast
import javax.inject.Inject
import team.ommaya.wequiz.android.home.quizlist.QuizList as QuizListComposable

private const val LeadingBackIconLayoutId = "LeadingBackIconLayout"
private const val TitleLayoutId = "TitleLayout"
private const val TrailingSearchIconLayoutId = "TrailingSearchIconLayout"

@AndroidEntryPoint
class QuizListActivity : ComponentActivity() {

    @Inject
    lateinit var getQuizListUseCase: GetQuizListUseCase

    @Inject
    lateinit var deleteQuizUseCase: DeleteQuizUseCase

    private val token by lazy { intent?.getStringExtra("token") ?: TmpToken }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var deleteModeEnable by remember { mutableStateOf(false) }
            var deleteIndexState by remember { mutableStateOf<Int?>(null) }

            var quizIds = remember { listOf<Int>() }
            var quizList by remember {
                mutableStateOf<PersistentList<QuizNameAndIsWritingPair>?>(null)
            }

            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(token) {
                quizList =
                    getQuizListUseCase(token, size = 30)
                        .getOrElse { exception ->
                            toast(exception.toString())
                            null
                        }
                        ?.quiz
                        ?.also { quiz ->
                            quizIds = quiz.map(Quiz::id)
                        }
                        ?.map { item ->
                            // TODO: 문제 만들기 상태인지 값이 안 넘어옴
                            Pair(item.title, false)
                        }
                        ?.toPersistentList()
            }

            QuizDeleteConfirmDialog(
                onDismissRequest = { deleteIndexState = null },
                deleteIndex = deleteIndexState,
                deleteAction = { index ->
                    deleteIndexState = null

                    coroutineScope.launch {
                        val result =
                            deleteQuizUseCase(
                                token = token,
                                quizId = quizIds[index],
                            )
                        if (result.isSuccess) {
                            quizList = quizList?.removeAt(index)
                        } else {
                            toast(result.exceptionOrNull()!!.toString())
                        }
                    }
                },
            )

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
                        BasicText(
                            modifier = Modifier.layoutId(TitleLayoutId),
                            text = "문제 리스트",
                            style = WeQuizTypography.B18
                                .change(color = WeQuizColor.G2)
                                .asRememberComposeStyle(),
                        )
                        Box(
                            Modifier
                                .layoutId(TrailingSearchIconLayoutId)
                                .size(24.dp)
                                .fitPaint(
                                    drawableId = R.drawable.ic_outline_search_24,
                                    colorFilter = WeQuizColor.G2.toRememberColorFilterOrNull(),
                                )
                                .noRippleClickable { toast("준비중...") },
                        )
                    },
                ) { measurables, constraints ->
                    val looseConstraints = constraints.asLoose(width = true, height = true)

                    val backIcon = measurables[LeadingBackIconLayoutId].measure(looseConstraints)
                    val title = measurables[TitleLayoutId].measure(looseConstraints)
                    val search = measurables[TrailingSearchIconLayoutId].measure(looseConstraints)

                    val width = constraints.maxWidth
                    val height = title.height + (15.dp.roundToPx() * 2)

                    layout(width = width, height = height) {
                        backIcon.place(
                            x = 12.dp.roundToPx(),
                            y = Alignment.CenterVertically.align(
                                size = backIcon.height,
                                space = height,
                            ),
                        )
                        title.place(
                            x = 12.dp.roundToPx() + backIcon.width + 4.dp.roundToPx(),
                            y = Alignment.CenterVertically.align(
                                size = title.height,
                                space = height,
                            ),
                        )
                        search.place(
                            x = width - 20.dp.roundToPx() - search.width,
                            y = Alignment.CenterVertically.align(
                                size = search.height,
                                space = height,
                            ),
                        )
                    }
                }

                @Suppress("NAME_SHADOWING")
                val quizList = quizList

                if (quizList != null) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 20.dp,
                                    start = 20.dp,
                                    end = 20.dp,
                                    bottom = 12.dp,
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            BasicText(
                                text = "전체 ${quizList.size}",
                                style = WeQuizTypography.M14
                                    .change(color = WeQuizColor.G2)
                                    .asRememberComposeStyle(),
                            )
                            BasicText(
                                modifier = Modifier.noRippleClickable {
                                    deleteModeEnable = !deleteModeEnable
                                },
                                text = if (deleteModeEnable) "완료" else "편집",
                                style = WeQuizTypography.R14
                                    .change(color = WeQuizColor.G2)
                                    .asRememberComposeStyle(),
                            )
                        }
                        QuizListComposable(
                            quizs = quizList,
                            deleteModeEnable = deleteModeEnable,
                            onDeleteIconClick = { index ->
                                deleteIndexState = index
                            },
                            onQuizClick = { index ->
                                startActivity(
                                    Intent(
                                        this@QuizListActivity,
                                        QuizDetailActivity::class.java,
                                    ).apply {
                                        putExtra("token", token)
                                        putExtra("quizId", quizIds[index])
                                    },
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizDeleteConfirmDialog(
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
                Box(
                    modifier = Modifier.padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = "해당 문제를 삭제할까요?",
                        style = WeQuizTypography.R16
                            .change(color = WeQuizColor.G2)
                            .asRememberComposeStyle(),
                    )
                }
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
