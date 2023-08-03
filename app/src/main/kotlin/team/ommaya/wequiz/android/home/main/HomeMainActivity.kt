/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
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
import team.ommaya.wequiz.android.data.client.TmpToken
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.design.resource.compose.WeQuizTypography
import team.ommaya.wequiz.android.domain.model.quiz.QuizList
import team.ommaya.wequiz.android.domain.model.rank.Rank
import team.ommaya.wequiz.android.domain.model.user.UserInformation
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizListUseCase
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizRankUseCase
import team.ommaya.wequiz.android.domain.usecase.user.GetUserInformationUseCase
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

    private val token by lazy { intent?.getStringExtra("token") ?: TmpToken }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var user by remember { mutableStateOf<UserInformation?>(null) }
            var quizRank by remember { mutableStateOf<Rank?>(null) }
            var quizList by remember { mutableStateOf<QuizList?>(null) }

            LaunchedEffect(token) {
                launch {
                    user = getUserInformationUseCase(token)
                        .getOrElse { exception ->
                            toast(exception.toString())
                            user
                        }
                }

                launch {
                    quizRank =
                        getQuizRankUseCase(token, size = 3)
                            .getOrElse { exception ->
                                toast(exception.toString())
                                quizRank
                            }
                }

                launch {
                    quizList =
                        getQuizListUseCase(token, size = 4)
                            .getOrElse { exception ->
                                toast(exception.toString())
                                quizList
                            }
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                BasicText(
                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp),
                    text = "LOGO",
                    style = WeQuizTypography.B18
                        .change(color = WeQuizColor.G2)
                        .asRememberComposeStyle(),
                )

                @Suppress("NAME_SHADOWING")
                val user = user

                @Suppress("NAME_SHADOWING")
                val quizRank = quizRank

                @Suppress("NAME_SHADOWING")
                val quizList = quizList

                if (user != null && quizRank != null && quizList != null) {
                    HomeMain(
                        nickname = user.data.nickname,
                        profileMessage = user.data.description,
                        profileImageSrc = R.drawable.ic_color_profile_image,
                        friendsRanking = remember(quizRank) {
                            quizRank
                                .rankings
                                .map { item ->
                                    Triple(item.userInfo.name, item.userInfo.id, item.score)
                                }
                                .toImmutableList()
                        },
                        quizs = remember(quizList) {
                            quizList
                                .quiz
                                .map { item ->
                                    // TODO: 문제 만들기 상태인지 값이 안 넘어옴
                                    Pair(item.title, false)
                                }
                                .toImmutableList()
                        },
                    )
                }
            }
        }
    }
}
