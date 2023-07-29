/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.statistic

import com.fasterxml.jackson.annotation.JsonProperty

internal data class QuestionsItem(
    @field:JsonProperty("questionId")
    val questionId: Int? = null,

    @field:JsonProperty("questionTitle")
    val questionTitle: String? = null,

    @field:JsonProperty("options")
    val options: List<OptionsItem?>? = null,
)
