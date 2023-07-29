/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.user

import com.fasterxml.jackson.annotation.JsonProperty

internal data class UserInformationFormattedResponse(
    @field:JsonProperty("code")
    val code: String? = null,

    @field:JsonProperty("data")
    val data: UserInformationResponse? = null,

    @field:JsonProperty("message")
    val message: String? = null,
)
