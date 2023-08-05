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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.domain.model.quiz.Answer
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetail
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetailOption
import team.ommaya.wequiz.android.domain.model.quiz.QuizDetailQuestion
import team.ommaya.wequiz.android.domain.model.quiz.QuizResult
import team.ommaya.wequiz.android.domain.usecase.quiz.SubmitQuizAnswerUseCase
import javax.inject.Inject

@HiltViewModel
class QuizSolveViewModel @Inject constructor(
    private val submitQuizAnswerUseCase: SubmitQuizAnswerUseCase,
) : ViewModel() {

    private val quizId: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _questionList: MutableStateFlow<List<QuizDetailQuestion>> =
        MutableStateFlow(emptyList())
    val questionList = _questionList.asStateFlow()

    private val _currentQuestion: MutableStateFlow<QuizDetailQuestion> = MutableStateFlow(
        QuizDetailQuestion(0, 0, emptyList(), 0, "")
    )
    val currentQuestion = _currentQuestion.asStateFlow()

    private val _result: MutableStateFlow<QuizResult> = MutableStateFlow(QuizResult())
    val result = _result.asStateFlow()

    private val totalAnswerList = MutableStateFlow<List<Answer>>(emptyList())

    private val currentSelectAnswerList = MutableStateFlow<List<Int>>(emptyList())

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

    val isAnswerSufficient = combine(
        currentQuestion,
        currentSelectAnswerList,
    ) { currentQuestion, currentSelectAnswerList ->
        if (currentQuestion.options.isNotEmpty()) {
            currentQuestion.answerCounts == currentSelectAnswerList.size
        } else {
            false
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false,
    )

    private val _quizSolveUiState: MutableSharedFlow<SolveUiState> = MutableSharedFlow()
    val quizSolveUiState = _quizSolveUiState.asSharedFlow()

    fun initQuiz(quiz: QuizDetail) {
        quizId.value = quiz.id
        _questionList.value = quiz.questions
        _currentQuestion.value = quiz.questions[currentQuestionCount.value]
        questionCount.value = quiz.questions.size
    }

    fun hasNexQuestion(): Boolean {
        val answerList = mutableListOf<Answer>().apply {
            addAll(totalAnswerList.value)
            add(
                Answer(
                    currentSelectAnswerList.value,
                    currentQuestion.value.id,
                )
            )
        }
        totalAnswerList.value = answerList
        return if (currentQuestionCount.value + 1 < questionCount.value) {
            currentQuestionCount.value += 1
            _currentQuestion.value = questionList.value[currentQuestionCount.value]
            currentSelectAnswerList.value = emptyList()
            true
        } else {
            false
        }
    }

    fun setCurrentAnswerList(item: QuizDetailOption, isChecked: Boolean) {
        val optionId = item.id
        val currentList = mutableListOf<Int>().apply {
            addAll(currentSelectAnswerList.value)
        }
        with(currentList) {
            if (isChecked) {
                if (!contains(optionId)) {
                    add(optionId)
                    sort()
                }
                currentSelectAnswerList.value = currentList
            } else {
                if (contains(optionId)) {
                    remove(optionId)
                    sort()
                }
                currentSelectAnswerList.value = currentList
            }
        }
    }

    fun submitAnswer() {
        viewModelScope.launch {
            _quizSolveUiState.emit(SolveUiState.Loading)
            submitQuizAnswerUseCase(quizId.value, totalAnswerList.value)
                .onSuccess {
                    _quizSolveUiState.emit(SolveUiState.Success(it))
                }.onFailure {
                    _quizSolveUiState.emit(SolveUiState.Fail(it.message ?: "네트워크 에러"))
                }
        }
    }
}

sealed interface SolveUiState {
    object Loading : SolveUiState
    data class Success(
        val data: QuizResult
    ) : SolveUiState

    data class Fail(
        val message: String
    ) : SolveUiState
}
