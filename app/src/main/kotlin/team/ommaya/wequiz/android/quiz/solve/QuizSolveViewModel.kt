/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetailQuestion

class QuizSolveViewModel : ViewModel() {

    private val _quiz: MutableStateFlow<List<QuizDetailQuestion>> = MutableStateFlow(emptyList())
    val quiz = _quiz.asStateFlow()

    private val _currentQuestion: MutableStateFlow<QuizDetailQuestion> = MutableStateFlow(
        QuizDetailQuestion(0, 0, emptyList(), 0, "")
    )
    val currentQuestion = _currentQuestion.asStateFlow()

    private val _isAnswerSufficient: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isAnswerSufficient = _isAnswerSufficient.asSharedFlow()

    private val _selectCount: MutableSharedFlow<Int> = MutableSharedFlow()
    val selectCount = _selectCount.asSharedFlow()

    private val score = MutableStateFlow(0)

    private val questionCount = MutableStateFlow(0)

    private val currentQuestionCount = MutableStateFlow(0)

    val currentAndTotalCount = combine(
        currentQuestionCount,
        questionCount,
    ) { current, total ->
        Pair(current, total)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        Pair(0, 0),
    )

    fun initQuiz(quiz: List<QuizDetailQuestion>) {
        _quiz.value = quiz
        _currentQuestion.value = quiz[currentQuestionCount.value]
        questionCount.value = quiz.size
    }

}