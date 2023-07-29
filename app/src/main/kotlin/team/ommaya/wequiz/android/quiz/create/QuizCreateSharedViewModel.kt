/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.domain.usecase.quiz.MakeInvitationLinkUseCase
import javax.inject.Inject

@HiltViewModel
class QuizCreateSharedViewModel @Inject constructor(
    private val makeInvitationLinkUseCase: MakeInvitationLinkUseCase,
) : ViewModel() {

    private val _quizCreateState: MutableStateFlow<QuizCreateState> =
        MutableStateFlow(QuizCreateState.CREATE)
    val quizCreateState = _quizCreateState.asStateFlow()

    private val _isQuizMeetRequireMeet: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isQuizMeetRequireMeet = _isQuizMeetRequireMeet.asStateFlow()

    private val _buttonEventState: MutableSharedFlow<QuizCreateState> = MutableSharedFlow()
    val buttonEventState = _buttonEventState.asSharedFlow()

    private val _quizId: MutableStateFlow<Int> = MutableStateFlow(0)
    val quizId = _quizId.asStateFlow()

    private val _quizLink: MutableSharedFlow<Uri> = MutableSharedFlow()
    val quizLink = _quizLink.asSharedFlow()

    fun setQuizCreateState(state: QuizCreateState) {
        _quizCreateState.value = state
    }

    fun setRequiredState(state: Boolean) {
        _isQuizMeetRequireMeet.value = state
    }

    fun setButtonEventState(state: QuizCreateState) {
        viewModelScope.launch {
            _buttonEventState.emit(state)
        }
    }

    fun setQuizId(quizId: Int) {
        _quizId.value = quizId
    }

    fun makeQuizLink() {
        viewModelScope.launch {
            makeInvitationLinkUseCase(quizId.value).collect {
                _quizLink.emit(it)
            }
        }
    }

    enum class QuizCreateState {
        CREATE, DONE,
    }
}
