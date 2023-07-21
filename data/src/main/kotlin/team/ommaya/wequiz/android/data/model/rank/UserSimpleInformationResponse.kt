/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.rank

import com.fasterxml.jackson.annotation.JsonProperty

data class UserSimpleInformationResponse(
    @field:JsonProperty("name")
    val name: String? = null,

    @field:JsonProperty("id")
    val id: Int? = null
)
