/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.domain.model.quiz.QuizList
import team.ommaya.wequiz.android.domain.model.rank.Rank
import team.ommaya.wequiz.android.domain.model.user.UserInformation
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizListUseCase
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizRankUseCase
import team.ommaya.wequiz.android.domain.usecase.user.GetUserInformationUseCase
import team.ommaya.wequiz.android.home.common.QuizDeleteExtraKey
import team.ommaya.wequiz.android.home.common.QuizDeleteResultCode
import team.ommaya.wequiz.android.home.friends.FriendsRankActivity
import team.ommaya.wequiz.android.home.quizdetail.QuizDetailActivity
import team.ommaya.wequiz.android.home.quizlist.QuizListActivity
import team.ommaya.wequiz.android.utils.fitPaint
import team.ommaya.wequiz.android.utils.toast
import javax.inject.Inject

@AndroidEntryPoint
class HomeMainActivity : ComponentActivity() {

    @Inject
    lateinit var getUserInformationUseCase: GetUserInformationUseCase

    @Inject
    lateinit var getQuizRankUseCase: GetQuizRankUseCase

    @Inject
    lateinit var getQuizListUseCase: GetQuizListUseCase

    private var quizList by mutableStateOf<QuizList?>(null)

    private val token =
        "AIE-54W-amwtn2V03BQXn5ibwu3my68KXVAL4b7wQMa7gIDLV_QGwcQji_5lQ30sV20L5igMhn4Daig6w4JhTPOF_rQ_c-CF5rojgpVw8EVKnNgJF2ePgAt4bRJ86Mvml51yWvWl2wcTX30StvIeSomDhlhUx2jcMw"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var user by remember { mutableStateOf<UserInformation?>(null) }
            var quizRank by remember { mutableStateOf<Rank?>(null) }

            LaunchedEffect(token) {
                launch {
                    user = getUserInformationUseCase(token)
                        .getOrElse { exception ->
                            toast(exception.toString())
                            exception.printStackTrace()
                            user
                        }
                }

                launch {
                    quizRank =
                        getQuizRankUseCase(token, size = 3)
                            .getOrElse { exception ->
                                toast(exception.toString())
                                exception.printStackTrace()
                                quizRank
                            }
                }

                launch {
                    quizList =
                        getQuizListUseCase(token, size = 4)
                            .getOrElse { exception ->
                                toast(exception.toString())
                                exception.printStackTrace()
                                quizList
                            }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = WeQuizColor.G9.value),
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 15.dp, horizontal = 20.dp)
                        .fitPaint(drawableId = R.drawable.ic_color_logo_banner),
                )

                @Suppress("NAME_SHADOWING")
                val user = user

                @Suppress("NAME_SHADOWING")
                val quizRank = quizRank

                @Suppress("NAME_SHADOWING")
                val quizList = quizList

                if (user != null) {
                    HomeMain(
                        nickname = user.nickname,
                        profileMessage = user.description,
                        profileImageSrc = R.drawable.ic_color_profile_image,
                        friendsRanking = remember(quizRank) {
                            quizRank
                                ?.rankings
                                ?.map { item ->
                                    Triple(item.userInfo.name, item.userInfo.id, item.score)
                                }
                                .orEmpty()
                                .toImmutableList()
                        },
                        quizs = remember(quizList) {
                            quizList
                                ?.quiz
                                ?.map { item ->
                                    // TODO: 문제 만들기 상태 연결
                                    Pair(item.title, false)
                                }
                                .orEmpty()
                                .toImmutableList()
                        },
                        onFriendRankingSectionClick = {
                            startActivity(
                                Intent(
                                    this@HomeMainActivity,
                                    FriendsRankActivity::class.java,
                                ).apply {
                                    putExtra("token", token)
                                }
                            )
                        },
                        onMyQuizSectionClick = {
                            startActivity(
                                Intent(
                                    this@HomeMainActivity,
                                    QuizListActivity::class.java,
                                ).apply {
                                    putExtra("token", token)
                                }
                            )
                        },
                        onQuizClick = { index ->
                            startActivity(
                                Intent(
                                    this@HomeMainActivity,
                                    QuizDetailActivity::class.java,
                                ).apply {
                                    putExtra("token", token)
                                    putExtra("quizId", quizList!!.quiz[index].id)
                                },
                            )
                        },
                    )
                }
            }
        }
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == QuizDeleteResultCode) {
            val deleteQuizIds = data!!.getIntegerArrayListExtra(QuizDeleteExtraKey)!!
            quizList =
                quizList
                    ?.copy(
                        quiz = quizList
                            ?.quiz
                            ?.toMutableList()
                            ?.apply {
                                removeIf { quiz ->
                                    deleteQuizIds.contains(quiz.id)
                                }
                            }
                            .orEmpty(),
                    )
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
