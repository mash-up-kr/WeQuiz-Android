/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.domain.model.quiz.Creator
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetail
import team.ommaya.wequiz.android.domain.model.quiz.QuizResult
import team.ommaya.wequiz.android.domain.model.rank.RankingsItem
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizDetailUseCase
import team.ommaya.wequiz.android.domain.usecase.user.GetUserUseCase
import javax.inject.Inject

@HiltViewModel
class QuizSolveSharedViewModel @Inject constructor(
    private val getQuizDetailUseCase: GetQuizDetailUseCase,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private val _isQuizValid: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isQuizValid = _isQuizValid.asSharedFlow()

    private val _quizDetail: MutableStateFlow<QuizDetail> =
        MutableStateFlow(QuizDetail(emptyList(), 0, Creator(), ""))
    val quizDetail = _quizDetail.asStateFlow()

    private val _isLogin: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLogin = _isLogin.asStateFlow()

    private val _userToken: MutableStateFlow<String> = MutableStateFlow("")
    val userToken = _userToken.asStateFlow()

    private val _result: MutableStateFlow<QuizResult> = MutableStateFlow(QuizResult())
    val result = _result.asStateFlow()

    private val _rankingList: MutableStateFlow<List<RankingsItem>> = MutableStateFlow(emptyList())
    val rankingList = _rankingList.asStateFlow()

    fun getQuizDetail(quizId: Int) {
        viewModelScope.launch {
            getQuizDetailUseCase(quizId)
                .onSuccess {
                    _isQuizValid.emit(true)
                    _quizDetail.value = it
                }.onFailure {
                    _isQuizValid.emit(false)
                }
        }
    }

    fun checkLogin() {
        viewModelScope.launch {
            with(getUserUseCase()) {
                _isLogin.value = isLogin
                _userToken.value = token
            }
        }
    }

    fun setToken(token: String) {
        _userToken.value = token
    }

    fun setResult(result: QuizResult, rankingList: List<RankingsItem>) {
        _result.value = result
        _rankingList.value = rankingList
    }
}
