/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.create.viewholder

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import team.ommaya.wequiz.android.quiz.create.Quiz

class QuizCreateViewModel : ViewModel() {

    private val _quizList: MutableStateFlow<List<Quiz>> = MutableStateFlow(emptyList())
    val quizList = _quizList.asStateFlow()


}