/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.statistic

import com.fasterxml.jackson.annotation.JsonProperty

internal data class QuizStatisticResponse(
    @field:JsonProperty("quizId")
    val quizId: Int? = null,

    @field:JsonProperty("questions")
    val questions: List<QuestionsItem?>? = null,

    @field:JsonProperty("quizTitle")
    val quizTitle: String? = null,
)
