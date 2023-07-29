/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.statistic

import com.fasterxml.jackson.annotation.JsonProperty

internal data class OptionsItem(
    @field:JsonProperty("selectivity")
    val selectivity: Float? = null,

    @field:JsonProperty("optionId")
    val optionId: Int? = null,

    @field:JsonProperty("content")
    val content: String? = null,

    @field:JsonProperty("isCorrect")
    val isCorrect: Boolean? = null,
)
