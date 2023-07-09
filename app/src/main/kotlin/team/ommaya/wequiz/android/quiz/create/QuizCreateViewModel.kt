/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

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

class QuizCreateViewModel : ViewModel() {

    private val initialQuestionList = listOf(
        Question(0),
        Question(1),
        Question(-1, type = Question.QuestionType.Add),
    )

    private val _questionList: MutableStateFlow<List<Question>> =
        MutableStateFlow(initialQuestionList)
    val questionList = _questionList.asStateFlow()

    private val _focusedPosition: MutableStateFlow<Int> = MutableStateFlow(-1)
    val focusedPosition = _focusedPosition.asStateFlow()

    fun addQuiz() {
        val currentSize = questionList.value.size
        val list = mutableListOf<Question>().apply {
            addAll(questionList.value)
        }
        if (currentSize == 10) {
            list[currentSize - 1] =
                list.last().copy(index = currentSize - 1, type = Question.QuestionType.Default)
        } else {
            list[list.lastIndex] =
                list.last().copy(index = list.lastIndex, type = Question.QuestionType.Default)
            list.add(Question(index = currentSize, type = Question.QuestionType.Add))
        }
        _questionList.value = list.toList()
    }

    fun addAnswer(questionPosition: Int) {
        val currentSize = questionList.value[questionPosition].answerList.size
        val list = mutableListOf<Answer>().apply {
            addAll(questionList.value[questionPosition].answerList)
        }
        if (currentSize == 5) {
            list[currentSize - 1] =
                list.last().copy(index = currentSize - 1, type = Answer.AnswerType.Default)
        } else {
            list[list.lastIndex] =
                list.last().copy(index = list.lastIndex, type = Answer.AnswerType.Default)
            list.add(Answer(index = currentSize, type = Answer.AnswerType.Add))
        }
        val questionList = mutableListOf<Question>().apply {
            addAll(questionList.value)
        }
        questionList.forEachIndexed { index, question ->
            if (index == questionPosition) {
                questionList[index] = question.copy(answerList = list)
            }
        }
        _questionList.update { questionList }
    }

    fun setQuestionFocus(questionPosition: Int) {
        _focusedPosition.value = questionPosition
    }

    fun getAnswerList(questionPosition: Int) = questionList.value[questionPosition].answerList
}
