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

    fun addQuestion() {
        val currentSize = questionList.value.size
        val currentQuestionList = mutableListOf<Question>().apply {
            addAll(questionList.value)
        }

        currentQuestionList.removeLast()
        currentQuestionList.add(makeQuestion())
        if (currentSize != MAX_QUESTION_COUNT) {
            currentQuestionList.add(makeQuestion(type = Question.QuestionType.Add))
        }

        _questionList.value = currentQuestionList.toList()
    }

    fun addAnswer(questionPosition: Int) {
        val currentSize = questionList.value[questionPosition].answerList.size
        val currentAnswerList = mutableListOf<Answer>().apply {
            addAll(questionList.value[questionPosition].answerList)
        }

        currentAnswerList.removeLast()
        currentAnswerList.add(makeAnswer())
        if (currentSize != MAX_ANSWER_COUNT) {
            currentAnswerList.add(makeAnswer(type = Answer.AnswerType.Add))
        }

        val questionList = mutableListOf<Question>().apply {
            addAll(questionList.value)
        }

        questionList.forEachIndexed { index, question ->
            if (index == questionPosition) {
                questionList[index] = question.copy(answerList = currentAnswerList)
            }
        }
        _questionList.update { questionList }
    }

    fun setQuestionFocus(questionPosition: Int, isQuestionItemClick: Boolean = false) {
        if (questionPosition == QUESTION_ADD_POSITION) {
            return
        }
        val list = mutableListOf<Question>().apply {
            addAll(questionList.value)
        }

        list.forEachIndexed { index, question ->
            if (index == questionPosition) {
                list[questionPosition] = list[questionPosition].copy(
                    isFocus = if (isQuestionItemClick) {
                        !list[questionPosition].isFocus
                    } else {
                        true
                    }
                )
            } else {
                list[index] = question.copy(isFocus = false)
            }
        }


        _questionList.update { list }
    }

    fun getAnswerList(questionPosition: Int) = questionList.value[questionPosition].answerList

    fun setMultipleChoice(questionPosition: Int) {
        val list = mutableListOf<Question>().apply {
            addAll(questionList.value)
        }

        list[questionPosition] =
            list[questionPosition].copy(isMultipleChoice = !list[questionPosition].isMultipleChoice)

        _questionList.update { list }
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

    fun deleteQuestion(questionPosition: Int) {
        val list = mutableListOf<Question>().apply {
            addAll(questionList.value)
            removeAt(questionPosition)
        }
        _questionList.value = list
    }

    companion object {
        const val QUESTION_ADD_POSITION = -1
        const val MAX_QUESTION_COUNT = 10
        const val MAX_ANSWER_COUNT = 5
    }
}
