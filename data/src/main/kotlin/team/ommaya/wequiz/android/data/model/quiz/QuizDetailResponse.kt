/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.quiz

import com.fasterxml.jackson.annotation.JsonProperty

internal data class QuizDetailResponse(
    @field:JsonProperty("questions")
    val questions: List<QuestionsItem?>? = null,

    @field:JsonProperty("id")
    val id: Int? = null,

    @field:JsonProperty("creator")
    val creator: Creator,

    @field:JsonProperty("title")
    val title: String? = null,
)
