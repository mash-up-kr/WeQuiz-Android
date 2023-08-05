/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.domain.model.quiz

data class QuizDetail(
    val questions: List<QuizDetailQuestion>,
    val id: Int,
    val creator: Creator,
    val title: String,
)

data class Creator(
    val id: Int = 0,
    val name: String = ""
)