/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.home.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.R
import team.ommaya.wequiz.android.design.resource.compose.WeQuizColor
import team.ommaya.wequiz.android.domain.model.quiz.QuizList
import team.ommaya.wequiz.android.domain.model.rank.Rank
import team.ommaya.wequiz.android.domain.model.user.UserInformation
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizListUseCase
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizRankUseCase
import team.ommaya.wequiz.android.domain.usecase.user.GetUserInformationUseCase
import team.ommaya.wequiz.android.domain.usecase.user.UserLogoutUseCase
import team.ommaya.wequiz.android.home.friends.FriendsRankActivity
import team.ommaya.wequiz.android.home.obtainToken
import team.ommaya.wequiz.android.home.quizdetail.QuizDetailActivity
import team.ommaya.wequiz.android.home.quizlist.QuizListActivity
import team.ommaya.wequiz.android.intro.IntroActivity
import team.ommaya.wequiz.android.quiz.create.QuizCreateActivity
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

    @Inject
    lateinit var userLogoutUseCase: UserLogoutUseCase

    private var quizList by mutableStateOf<QuizList?>(null)
    private var user by mutableStateOf<UserInformation?>(null)
    private var quizRank by mutableStateOf<Rank?>(null)

    private val token by lazy { obtainToken() }

    private var waitTime = mutableLongStateOf(0L)

    private val finishToast by lazy {
        Toast.makeText(
            this@HomeMainActivity,
            "한번 더 누르면 종료됩니다.",
            Toast.LENGTH_SHORT,
        )
    }

    override fun onRestart() {
        super.onRestart()
        lifecycleScope.launch {
            loadResources()
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - waitTime.longValue >= 2000) {
                waitTime.longValue = System.currentTimeMillis()
                finishToast.show()
            } else {
                finishToast.cancel()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)
        setContent {
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(token) {
                loadResources()
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

                val user = user
                val quizRank = quizRank
                val quizList = quizList

                if (user != null) {
                    HomeMain(
                        nickname = user.data.nickname,
                        profileMessage = user.data.description,
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
                                    putExtra(IntroActivity.TOKEN, token)
                                },
                            )
                        },
                        onMyQuizSectionClick = {
                            startActivity(
                                Intent(
                                    this@HomeMainActivity,
                                    QuizListActivity::class.java,
                                ).apply {
                                    putExtra(IntroActivity.TOKEN, token)
                                },
                            )
                        },
                        onQuizCreateClick = {
                            startActivity(
                                Intent(
                                    this@HomeMainActivity,
                                    QuizCreateActivity::class.java,
                                ),
                            )
                        },
                        onQuizClick = { index ->
                            startActivity(
                                Intent(
                                    this@HomeMainActivity,
                                    QuizDetailActivity::class.java,
                                ).apply {
                                    putExtra(IntroActivity.TOKEN, token)
                                    putExtra("quizId", quizList!!.quiz[index].id)
                                },
                            )
                        },
                        onLogoutClick = {
                            coroutineScope.launch {
                                userLogoutUseCase()
                                    .onSuccess {
                                        finish()
                                        Firebase.auth.signOut()
                                        toast("로그아웃 되었어요.")
                                        startActivity(
                                            Intent(
                                                this@HomeMainActivity,
                                                IntroActivity::class.java,
                                            ),
                                        )
                                    }
                                    .onFailure { exception ->
                                        toast("로그아웃에 실패했어요.")
                                        exception.printStackTrace()
                                    }
                            }
                        },
                    )
                }
            }
        }
    }

    private fun CoroutineScope.loadResources() {
        launch {
            user =
                getUserInformationUseCase(token)
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

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}
