/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.domain.usecase.quiz.CreateQuizUseCase
import team.ommaya.wequiz.android.quiz.create.Answer.Companion.makeAnswer
import team.ommaya.wequiz.android.quiz.create.Question.Companion.makeQuestion
import javax.inject.Inject

@HiltViewModel
class QuizCreateViewModel @Inject constructor(
    private val createQuizUseCase: CreateQuizUseCase,
) : ViewModel() {

    private val _questionList: MutableStateFlow<List<Question>> =
        MutableStateFlow(Question.getInitialQuestionList())
    val questionList = _questionList.asStateFlow()

    private val _deleteQuestionAction: MutableSharedFlow<Question> = MutableSharedFlow()
    val deleteQuestionAction = _deleteQuestionAction.asSharedFlow()

    private val _createState: MutableSharedFlow<CreateState> = MutableSharedFlow()
    val createState = _createState.asSharedFlow()

    private val isAnswerCountRequired: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val isAnswerCorrectCountRequired: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val isQuestionCountRequired: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val quizTitle: MutableStateFlow<String> = MutableStateFlow("")

    private val _quizId: MutableStateFlow<Int> = MutableStateFlow(0)
    val quizId = _quizId.asStateFlow()

    val isQuizMeetRequireMeet =
        combine(
            isAnswerCountRequired,
            isAnswerCorrectCountRequired,
            isQuestionCountRequired,
            quizTitle,
        ) { isAnswerCountRequired, isAnswerCorrectCountRequired, isQuestionCountRequired, quizTitle ->
            isAnswerCountRequired && isAnswerCorrectCountRequired && isQuestionCountRequired && quizTitle.isNotBlank()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            false,
        )

    private var questionCount = 0

    fun createQuiz(title: String, questions: List<Question>) {
        viewModelScope.launch {
            _createState.emit(CreateState.Loading)
            createQuizUseCase(title, questions.toQuestionDomainList())
                .onSuccess {
                    _quizId.value = it
                    _createState.emit(CreateState.Success)
                }.onFailure {
                    _createState.emit(CreateState.Fail(it.message?: "네트워크 에러"))
                }
        }
    }

    fun setQuizTitle(title: String) {
        quizTitle.value = title
    }

    fun addQuestion() {
        val currentSize = questionList.value.size
        val currentQuestionList = getCurrentQuestionList()

        currentQuestionList.removeLast()
        currentQuestionList.add(makeQuestion())
        if (currentSize != MAX_QUESTION_COUNT) {
            currentQuestionList.add(makeQuestion(type = Question.QuestionType.Add))
        }

        _questionList.value = currentQuestionList.toList()
    }

    fun addAnswer(questionPosition: Int) {
        val currentSize = questionList.value[questionPosition].answerList.size
        val currentAnswerList = getCurrentAnswerList(questionPosition)

        _questionList.update {
            currentAnswerList.removeLast()
            currentAnswerList.add(makeAnswer())
            if (currentSize != MAX_ANSWER_COUNT) {
                currentAnswerList.add(makeAnswer(type = Answer.AnswerType.Add))
            }

            val currentQuestionList = getCurrentQuestionList()

            currentQuestionList.forEachIndexed { index, question ->
                if (index == questionPosition) {
                    currentQuestionList[index] = question.copy(answerList = currentAnswerList)
                }
            }
            currentQuestionList
        }
    }

    fun setQuestionFocus(questionPosition: Int, isQuestionItemClick: Boolean = false) {
        if (questionPosition == QUESTION_ADD_POSITION) {
            return
        }
        val currentQuestionList = getCurrentQuestionList()

        _questionList.update {
            currentQuestionList.forEachIndexed { index, question ->
                if (index == questionPosition) {
                    currentQuestionList[questionPosition] =
                        currentQuestionList[questionPosition].copy(
                            isFocus = if (isQuestionItemClick) {
                                !currentQuestionList[questionPosition].isFocus
                            } else {
                                true
                            },
                        )
                } else {
                    currentQuestionList[index] = question.copy(isFocus = false)
                }
            }
            currentQuestionList
        }
    }

    fun getAnswerList(questionPosition: Int) = questionList.value[questionPosition].answerList

    fun setMultipleChoice(questionPosition: Int) {
        val currentQuestionList = getCurrentQuestionList()
        val currentAnswerList = getCurrentAnswerList(questionPosition)

        _questionList.update {
            if (currentQuestionList[questionPosition].isMultipleChoice) {
                currentAnswerList.forEachIndexed { index, answer ->
                    currentAnswerList[index] = answer.copy(isCorrect = false)
                }
            }

            if (currentAnswerList.size > MIN_ANSWER_COUNT + 1) {
                currentQuestionList[questionPosition] =
                    currentQuestionList[questionPosition].copy(
                        isMultipleChoice = !currentQuestionList[questionPosition].isMultipleChoice,
                        answerList = currentAnswerList,
                    )
            }
            currentQuestionList
        }
    }

    fun setQuestionTitle(syncedQuestionPosition: Int, title: String) {
        val currentQuestionList = getCurrentQuestionList()

        _questionList.update {
            currentQuestionList.forEachIndexed { index, question ->
                if (syncedQuestionPosition == index) {
                    currentQuestionList[index] = question.copy(title = title)
                }
            }
            currentQuestionList
        }
    }

    fun setAnswerTitle(questionPosition: Int, answerPosition: Int, content: String) {
        _questionList.update { currentQuestionList ->
            currentQuestionList.mapIndexed { questionIndex, question ->
                if (questionIndex == questionPosition) {
                    question.copy(
                        answerList = question.answerList.mapIndexed { answerIndex, answer ->
                            if (answerIndex == answerPosition) {
                                answer.copy(content = content)
                            } else {
                                answer
                            }
                        },
                    )
                } else {
                    question
                }
            }
        }
    }

    fun setDeleteQuestionElement(question: Question) {
        viewModelScope.launch {
            _deleteQuestionAction.emit(question)
        }
    }

    fun deleteQuestion(question: Question) {
        _questionList.update {
            val currentQuestionList = getCurrentQuestionList()
            currentQuestionList.remove(getSyncedQuestion(question))
            if (currentQuestionList.size == MAX_QUESTION_COUNT - 1) {
                if (currentQuestionList.last().type == Question.QuestionType.Default) {
                    currentQuestionList.add(makeQuestion(Question.QuestionType.Add))
                }
            }
            currentQuestionList
        }
    }

    fun isQuestionListModified(): Boolean {
        val isModified = if (questionCount == questionList.value.size) {
            questionCount == MAX_QUESTION_COUNT
        } else {
            true
        }
        questionCount = questionList.value.size
        return isModified
    }

    fun getQuestionItemPosition(item: Question): Int {
        var position = 0
        questionList.value.forEachIndexed { index, question ->
            if (question.key == item.key) {
                position = index
            }
        }
        return position
    }

    fun getSyncedQuestion(item: Question): Question {
        val position = getQuestionItemPosition(item)
        return questionList.value[position]
    }

    fun getSyncedQuestionPosition(item: Question) = getQuestionItemPosition(getSyncedQuestion(item))

    fun setAnswerCorrect(syncedQuestion: Question, answerPosition: Int) {
        val currentQuestionList = getCurrentQuestionList()
        val currentAnswerList = getCurrentAnswerList(getQuestionItemPosition(syncedQuestion))

        _questionList.update {
            if (syncedQuestion.isMultipleChoice.not()) {
                currentAnswerList.forEachIndexed { index, currentAnswer ->
                    currentAnswerList[index] =
                        currentAnswer.copy(isCorrect = currentAnswer.key == currentAnswerList[answerPosition].key)
                }
            } else {
                currentAnswerList.forEachIndexed { index, currentAnswer ->
                    if (currentAnswer.key == currentAnswerList[answerPosition].key) {
                        currentAnswerList[index] =
                            currentAnswer.copy(isCorrect = !currentAnswer.isCorrect)
                    }
                }
            }
            currentQuestionList.forEachIndexed { index, currentQuestion ->
                if (index == getQuestionItemPosition(syncedQuestion)) {
                    currentQuestionList[index] =
                        currentQuestion.copy(answerList = currentAnswerList)
                }
            }
            currentQuestionList
        }
    }

    fun checkQuizRequirements(): List<Question> {
        val currentQuestion = getCurrentQuestionList()

        currentQuestion.forEachIndexed { index, question ->
            if (index != currentQuestion.size - 1) {
                val filteredList = getCurrentAnswerList(index).filter { it.content.isNotBlank() }
                isAnswerCountRequired.value = filteredList.size >= MIN_ANSWER_COUNT
                isAnswerCorrectCountRequired.value = filteredList.any { it.isCorrect }
                currentQuestion[index] = question.copy(answerList = filteredList)
            }
        }
        val filteredQuiz =
            currentQuestion.filter { it.title != "문제입력" }.filter { it.title.isNotBlank() }
        isQuestionCountRequired.value = filteredQuiz.size >= MIN_QUESTION_COUNT

        return filteredQuiz
    }

    private fun getCurrentQuestionList() = mutableListOf<Question>().apply {
        addAll(questionList.value)
    }

    private fun getCurrentAnswerList(questionPosition: Int) = mutableListOf<Answer>().apply {
        addAll(questionList.value[questionPosition].answerList)
    }

    /*enum class CreateState {
        SUCCESS, FAILED, LOADING,
    }*/

    sealed interface CreateState {
        object Loading : CreateState
        object Success : CreateState
        data class Fail(
            val message: String,
        ) : CreateState
    }

    companion object {
        const val QUESTION_ADD_POSITION = -1
        const val MAX_QUESTION_COUNT = 10
        const val MAX_ANSWER_COUNT = 5
        const val MIN_QUESTION_COUNT = 2
        const val MIN_ANSWER_COUNT = 2
    }
}
