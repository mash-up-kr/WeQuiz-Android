/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.quiz

import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionsItem(
    @field:JsonProperty("score")
    val score: Int? = null,

    @field:JsonProperty("answerCounts")
    val answerCounts: Int? = null,

    @field:JsonProperty("options")
    val options: List<OptionsItem?>? = null,

    @field:JsonProperty("id")
    val id: Int? = null,

    @field:JsonProperty("title")
    val title: String? = null,
)
