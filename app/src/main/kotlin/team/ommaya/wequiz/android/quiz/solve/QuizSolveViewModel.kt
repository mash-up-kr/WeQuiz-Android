/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class QuizSolveViewModel : ViewModel() {

    private val questionCount = MutableStateFlow(0)

    private val currentQuestionCount = MutableStateFlow(1)

    val currentAndTotalCount = combine(
        currentQuestionCount,
        questionCount,
    ) { current, total ->
        Pair(current, total)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        Pair(1, 0),
    )

    fun setQuestionCount(count: Int) {
        questionCount.value = count
    }
}