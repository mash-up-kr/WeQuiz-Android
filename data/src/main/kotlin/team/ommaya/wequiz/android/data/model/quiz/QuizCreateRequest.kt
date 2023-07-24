/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.quiz

import kotlinx.serialization.Serializable

@Serializable
data class QuizCreateRequest(
    val title: String = "",
    val questions: List<QuestionDto> = emptyList(),
)

@Serializable
data class QuestionDto(
    val title: String = "",
    val priority: Int = 0,
    val duplicatedOption: Int = 0,
    val options: List<OptionDto> = emptyList(),
)

@Serializable
data class OptionDto(
    val content: String = "",
    val priority: Int = 0,
    val isCorrect: Boolean = false,
)
