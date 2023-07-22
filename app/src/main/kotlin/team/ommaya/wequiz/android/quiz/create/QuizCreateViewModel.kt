/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import team.ommaya.wequiz.android.quiz.create.Answer.Companion.makeAnswer
import team.ommaya.wequiz.android.quiz.create.Question.Companion.makeQuestion

class QuizCreateViewModel : ViewModel() {

    private val _questionList: MutableStateFlow<List<Question>> =
        MutableStateFlow(Question.getInitialQuestionList())
    val questionList = _questionList.asStateFlow()

    private val _isEditMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    private var questionCount = 0

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

        currentAnswerList.removeLast()
        currentAnswerList.add(makeAnswer())
        if (currentSize != MAX_ANSWER_COUNT) {
            currentAnswerList.add(makeAnswer(type = Answer.AnswerType.Add))
        }

        val currentQuestionList = getCurrentQuestionList()

        _questionList.update {
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
                            }
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
        val list = mutableListOf<Question>().apply {
            addAll(questionList.value)
        }

        _questionList.update {
            list[questionPosition] =
                list[questionPosition].copy(isMultipleChoice = !list[questionPosition].isMultipleChoice)
            list
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
                        }
                    )
                } else {
                    question
                }
            }
        }
    }


    fun setEditMode() {
        _isEditMode.value = !isEditMode.value
        if (isEditMode.value) {
            val list = mutableListOf<Question>().apply {
                addAll(questionList.value)
            }

            list.forEachIndexed { index, question ->
                list[index] = question.copy(isFocus = false)
            }

            _questionList.update { list }
        }
    }

    fun deleteQuestion(question: Question) {
        val list = mutableListOf<Question>().apply {
            addAll(questionList.value)
            remove(getSyncedQuestion(question))
        }
        _questionList.value = list
    }

    fun isQuestionListModified(): Boolean {
        val isModified = if (questionCount == questionList.value.size) {
            isQuestionFull()
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

    private fun getCurrentQuestionList() = mutableListOf<Question>().apply {
        addAll(questionList.value)
    }

    private fun getCurrentAnswerList(questionPosition: Int) = mutableListOf<Answer>().apply {
        addAll(questionList.value[questionPosition].answerList)
    }

    private fun isQuestionFull() = questionList.value.last().type != Question.QuestionType.Add

    companion object {
        const val QUESTION_ADD_POSITION = -1
        const val MAX_QUESTION_COUNT = 10
        const val MAX_ANSWER_COUNT = 5
    }
}
