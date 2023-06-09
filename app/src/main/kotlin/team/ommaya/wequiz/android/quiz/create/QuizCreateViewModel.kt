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

class QuizCreateViewModel : ViewModel() {

    private val initialList = listOf(
        Quiz(0),
        Quiz(1),
        Quiz(-1, type = Quiz.QuizType.Add),
    )

    private val _quizList: MutableStateFlow<List<Quiz>> = MutableStateFlow(initialList)
    val quizList = _quizList.asStateFlow()

    fun addQuiz() {
        val currentSize = quizList.value.size
        val list = mutableListOf<Quiz>().apply {
            addAll(quizList.value)
        }
        if (currentSize == 10) {
            list[currentSize - 1] =
                list.last().copy(index = currentSize - 1, type = Quiz.QuizType.Create)
        } else {
            list[list.lastIndex] =
                list.last().copy(index = list.lastIndex, type = Quiz.QuizType.Create)
            list.add(Quiz(index = list.size, type = Quiz.QuizType.Add))
        }
        _quizList.value = list.toList()
    }
}
