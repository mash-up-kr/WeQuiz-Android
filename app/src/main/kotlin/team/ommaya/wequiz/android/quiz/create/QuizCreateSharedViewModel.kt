/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizCreateSharedViewModel : ViewModel() {

    private val _quizCreateState: MutableStateFlow<QuizCreateState> =
        MutableStateFlow(QuizCreateState.CREATE)
    val quizCreateState = _quizCreateState.asStateFlow()

    private val _isQuizMeetRequireMeet: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isQuizMeetRequireMeet = _isQuizMeetRequireMeet.asStateFlow()

    private val _buttonEventState: MutableSharedFlow<QuizCreateState> = MutableSharedFlow()
    val buttonEventState = _buttonEventState.asSharedFlow()

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

    enum class QuizCreateState {
        CREATE, DONE,
    }
}
