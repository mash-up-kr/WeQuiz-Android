/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.model.rank

import com.fasterxml.jackson.annotation.JsonProperty

internal data class RankingsItemResponse(
    @field:JsonProperty("userInfoDto")
    val userInfo: UserSimpleInformationResponse? = null,

    @field:JsonProperty("score")
    val score: Int? = null,
)
