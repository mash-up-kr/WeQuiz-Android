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
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetail
import team.ommaya.wequiz.android.domain.usecase.quiz.GetQuizDetailUseCase
import javax.inject.Inject

@HiltViewModel
class QuizSolveSharedViewModel @Inject constructor(
    private val getQuizDetailUseCase: GetQuizDetailUseCase,
) : ViewModel() {

    private val _isQuizValid: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isQuizValid = _isQuizValid.asSharedFlow()

    private val _quizDetail: MutableStateFlow<QuizDetail> =
        MutableStateFlow(QuizDetail(emptyList(), 0, ""))
    val quizDetail = _quizDetail.asStateFlow()

    fun getQuizDetail(quizId: Int) {
        viewModelScope.launch {
            getQuizDetailUseCase(quizId)
                .onSuccess {
                    _isQuizValid.emit(true)
                }.onFailure {
                    _isQuizValid.emit(false)
                }
        }
    }
}